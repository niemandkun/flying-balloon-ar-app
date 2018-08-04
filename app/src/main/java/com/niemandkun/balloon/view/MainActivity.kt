package com.niemandkun.balloon.view

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.ArFragment
import com.niemandkun.balloon.R
import com.niemandkun.balloon.math.Vector3
import com.niemandkun.balloon.model.Constants
import com.niemandkun.balloon.model.GameStage
import com.niemandkun.balloon.model.GameStageFactory
import com.niemandkun.balloon.util.SoundMeter
import com.niemandkun.balloon.util.getViewDirection

class MainActivity : AppCompatActivity() {
    private val mSoundMeter = SoundMeter()

    private val mHandler = Handler(Looper.getMainLooper())

    private var mGameStageIsReady = false

    private lateinit var mGameStage: GameStage

    private lateinit var mArFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mArFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment

        GameStageFactory.createScene(this)
                .thenAccept {
                    mGameStage = it
                    mArFragment.setOnTapArPlaneListener { hitResult, _, _ ->
                        tryDeployGameStage(hitResult)
                    }
                }
    }

    private fun tryDeployGameStage(hitResult: HitResult) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(mArFragment.arSceneView.scene)
        mGameStage.setParent(anchorNode)
        mArFragment.setOnTapArPlaneListener(null)
        mGameStageIsReady = true
    }

    private fun checkSoundAmplitude() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            val amplitude = mSoundMeter.getAmplitude()
            val force = amplitude / Constants.SOUND_TO_FORCE_RATIO

            if (amplitude > Constants.SOUND_THRESHOLD) {
                val displayPose = mArFragment.arSceneView.arFrame.camera.displayOrientedPose

                val viewDirection = displayPose.getViewDirection()
                        .normalize()

                val displayToBalloon = Vector3.fromSceneform(mGameStage.balloon.worldPosition)
                        .sub(Vector3.fromFloatArray(displayPose.translation))
                        .normalize()

                val cosine = viewDirection.dot(displayToBalloon)

                if (cosine > 0) {
                    val vectorForce = viewDirection.mul(cosine).mul(force).setY(0.0f)
                    mGameStage.balloon.applyForce(vectorForce)
                }
            }

            mHandler.postDelayed(this::checkSoundAmplitude, Constants.SOUND_CHECK_INTERVAL_MS)
        }
    }

    override fun onResume() {
        super.onResume()
        mSoundMeter.start()
        mHandler.post(this::checkSoundAmplitude)
    }

    override fun onPause() {
        super.onPause()
        mSoundMeter.stop()
    }
}

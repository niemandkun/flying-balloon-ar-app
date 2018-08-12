package com.niemandkun.balloon.view

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.ux.ArFragment
import com.niemandkun.balloon.R
import com.niemandkun.balloon.math.Vector3
import com.niemandkun.balloon.model.Constants
import com.niemandkun.balloon.model.GameStage
import com.niemandkun.balloon.model.GameStageFactory
import com.niemandkun.balloon.util.SoundMeter
import com.niemandkun.balloon.util.formatSecondsForTimer
import com.niemandkun.balloon.util.getViewDirection
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private val mSoundMeter = SoundMeter()

    private val mHandler = Handler(Looper.getMainLooper())

    private var mGameStageIsReady = false

    private lateinit var mGameStage: GameStage

    private lateinit var mArFragment: ArFragment

    private lateinit var mTimer: TextView

    private lateinit var mCountdown: TextView

    private lateinit var mResultMenu: View

    private lateinit var mResultTime: TextView

    private lateinit var mPlayAgainButton: Button

    private lateinit var mExitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mArFragment = supportFragmentManager.findFragmentById(R.id.main_ar_fragment) as ArFragment

        mTimer = findViewById(R.id.main_timer)
        mCountdown = findViewById(R.id.main_countdown)
        mResultMenu = findViewById(R.id.main_result_menu)
        mResultTime = findViewById(R.id.main_result_time)
        mPlayAgainButton = findViewById(R.id.main_result_play_again)
        mExitButton = findViewById(R.id.main_result_exit)

        GameStageFactory.createScene(this)
                .thenAccept {
                    mGameStage = it
                    mGameStage.onUpdateListener = { time -> mTimer.text = formatSecondsForTimer(time) }
                    mGameStage.onGameFinishListener = { time ->
                        mTimer.visibility = View.GONE
                        mResultMenu.visibility = View.VISIBLE
                        mResultTime.text = getString(R.string.time_result, formatSecondsForTimer(time))
                    }
                    mArFragment.setOnTapArPlaneListener { hitResult, _, _ -> tryDeployGameStage(hitResult) }
                }

        mTimer.text = formatSecondsForTimer(0.0f)
        mExitButton.setOnClickListener { _ -> finish() }
        mPlayAgainButton.setOnClickListener { _ -> restartMainActivity() }
    }

    private fun restartMainActivity() {
        val selfIntent = intent
        finish()
        startActivity(selfIntent)
    }

    private fun tryDeployGameStage(hitResult: HitResult) {
        val anchor = hitResult.createAnchor()
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(mArFragment.arSceneView.scene)
        mGameStage.setParent(anchorNode)
        mArFragment.setOnTapArPlaneListener(null)
        mHandler.postDelayed({ showCountdown(3) }, 1000)
    }

    private fun showCountdown(number: Int) {
        if (number == -1) {
            mCountdown.visibility = View.GONE
            return
        }
        mTimer.visibility = View.VISIBLE
        mCountdown.visibility = View.VISIBLE
        mHandler.postDelayed({ showCountdown(number - 1) }, 1000)
        if (number != 0) {
            mCountdown.text = number.toString()
            return
        }
        mCountdown.text = getString(R.string.start)
        mGameStageIsReady = true
        mGameStage.beginTimeMeasurement()
    }

    private fun checkSoundAmplitude() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            mHandler.postDelayed(this::checkSoundAmplitude, Constants.SOUND_CHECK_INTERVAL_MS)

            if (!mGameStageIsReady) {
                return
            }

            val amplitude = mSoundMeter.getAmplitude()
            val force = amplitude / Constants.SOUND_TO_FORCE_RATIO

            if (amplitude < Constants.SOUND_THRESHOLD) {
                return
            }

            val cameraPose = mArFragment.arSceneView.arFrame.camera.pose

            val viewDirection = cameraPose.getViewDirection().normalize()

            val cameraToBalloon = Vector3.fromSceneform(mGameStage.balloon.worldPosition)
                    .sub(Vector3.fromFloatArray(cameraPose.translation));

            val cosine = viewDirection.dot(cameraToBalloon.normalize())

            val distanceFade = Math.E.toFloat().pow(-Constants.DISTANCE_FADE_RATIO * cameraToBalloon.length())

            if (cosine > 0) {
                val vectorForce = viewDirection.mul(cosine).mul(force).mul(distanceFade).setY(0.0f)
                mGameStage.balloon.applyForce(vectorForce)
            }
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

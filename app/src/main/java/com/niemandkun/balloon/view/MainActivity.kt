package com.niemandkun.balloon.view

import android.arch.lifecycle.Lifecycle
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.niemandkun.balloon.BuildConfig
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

    private lateinit var mArFragment: MyArFragment

    private lateinit var mTimer: TextView

    private lateinit var mCountdown: TextView

    private lateinit var mResultMenu: View

    private lateinit var mResultTime: TextView

    private lateinit var mPlayAgainButton: Button

    private lateinit var mExitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mArFragment = supportFragmentManager.findFragmentById(R.id.main_ar_fragment) as MyArFragment

        mTimer = findViewById(R.id.main_timer)
        mCountdown = findViewById(R.id.main_countdown)
        mResultMenu = findViewById(R.id.main_result_menu)
        mResultTime = findViewById(R.id.main_result_time)
        mPlayAgainButton = findViewById(R.id.main_result_play_again)
        mExitButton = findViewById(R.id.main_result_exit)

        GameStageFactory.createScene(this).thenAccept { onGameStageLoadFinished(it) }

        mTimer.text = formatSecondsForTimer(0.0f)
        mExitButton.setOnClickListener { finish() }
        mPlayAgainButton.setOnClickListener { restartMainActivity() }
    }

    private fun onGameStageLoadFinished(gameStage: GameStage) {
        mGameStage = gameStage
        mGameStage.onUpdateListener = { time -> mTimer.text = formatSecondsForTimer(time) }
        mGameStage.onGameFinishListener = { time ->
            mTimer.visibility = View.GONE
            mResultMenu.visibility = View.VISIBLE
            mResultTime.text = getString(R.string.time_result, formatSecondsForTimer(time))
        }
        attachBalloonSpawnTrigger();
    }

    private fun attachBalloonSpawnTrigger() {
        if (BuildConfig.USE_AUGMENTED_IMAGES) {
            attachAugmentedImagesSpawnTrigger()
        } else {
            attachOnTapSpawnTrigger()
        }
    }

    private fun attachAugmentedImagesSpawnTrigger() {
        mArFragment.setOnAugmentedImageFoundListener { images ->
            images.firstOrNull { it.name == "balloon.png" }
                ?.let { it.createAnchor(it.centerPose) }
                ?.apply { tryDeployGameStage(this) }
        }
    }

    private fun attachOnTapSpawnTrigger() {
        mArFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            tryDeployGameStage(hitResult.createAnchor())
        }
    }

    private fun restartMainActivity() {
        val selfIntent = intent
        finish()
        startActivity(selfIntent)
    }

    private fun tryDeployGameStage(anchor: Anchor) {
        val anchorNode = AnchorNode(anchor)
        anchorNode.setParent(mArFragment.arSceneView.scene)
        mGameStage.setParent(anchorNode)
        mArFragment.setOnTapArPlaneListener(null)
        mArFragment.setOnAugmentedImageFoundListener(null)
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

    private fun gameLoop() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            mHandler.postDelayed(this::gameLoop, Constants.SOUND_CHECK_INTERVAL_MS)
            if (mGameStageIsReady) {
                onGameLoopIteration()
            }
        }
    }

    private fun onGameLoopIteration() {
        val frame = mArFragment.arSceneView.arFrame ?: return

        val amplitude = mSoundMeter.getAmplitude()
        val force = amplitude / Constants.SOUND_TO_FORCE_RATIO

        if (amplitude < Constants.SOUND_THRESHOLD) {
            return
        }

        val viewDirection = frame.androidSensorPose.getViewDirection().normalize()

        val cameraToBalloon = Vector3.fromSceneform(mGameStage.balloon.worldPosition)
            .sub(Vector3.fromFloatArray(frame.camera.displayOrientedPose.translation))

        val cosine = viewDirection.dot(cameraToBalloon.normalize())

        val distanceFade = Math.E.toFloat().pow(-Constants.DISTANCE_FADE_RATIO * cameraToBalloon.length())

        if (cosine > 0) {
            val vectorForce = viewDirection.setY(0.0f).mul(cosine).mul(force).mul(distanceFade)
            mGameStage.balloon.applyForce(vectorForce)
        }
    }

    override fun onResume() {
        super.onResume()
        mSoundMeter.start()
        mHandler.post(this::gameLoop)
    }

    override fun onPause() {
        super.onPause()
        mSoundMeter.stop()
    }
}

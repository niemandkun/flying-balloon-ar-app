package com.niemandkun.balloon.model

import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.niemandkun.balloon.math.Vector3

class GameStage(
        balloonRenderable: Renderable,
        portalRenderable: Renderable,
        speedometerRenderable: ViewRenderable
) : Node() {
    val balloon = GameObject(Constants.BALLOON_MASS, Constants.AIR_FRICTION, balloonRenderable).apply {
        worldPosition = Vector3.UP.toSceneform()
    }

    val portal = GameObject(Constants.PORTAL_MASS, Constants.AIR_FRICTION, portalRenderable).apply {
        worldPosition = Vector3.FORWARD.mul(Constants.DISTANCE_TO_PORTAL).add(Vector3.UP.mul(0.7f)).toSceneform()
    }

    private val speedometer = Speedometer(speedometerRenderable).apply {
        localPosition = Vector3.UP.mul(0.6f).toSceneform()
    }

    private var mTimeMeasurementStart = 0f

    private var mBeginTimeMeasurement = false

    private var mTimeIsBeingMeasured = false

    var onUpdateListener: ((Float) -> Unit)? = null

    var onGameFinishListener: ((Float) -> Unit)? = null

    init {
        balloon.addChild(speedometer)
        addChild(balloon)
        addChild(portal)
    }

    override fun onUpdate(frameTime: FrameTime) {
        super.onUpdate(frameTime)

        if (!mTimeIsBeingMeasured && mBeginTimeMeasurement) {
            mBeginTimeMeasurement = false
            mTimeIsBeingMeasured = true
            mTimeMeasurementStart = frameTime.startSeconds
        }

        if (mTimeIsBeingMeasured) {
            onUpdateListener?.invoke(frameTime.startSeconds - mTimeMeasurementStart)
        }

        val balloonToPortalDistance = Vector3.fromSceneform(balloon.worldPosition).keepXZ()
                .sub(Vector3.fromSceneform(portal.worldPosition).keepXZ())
                .length()

        if (balloonToPortalDistance < Constants.BALLOON_TO_PORTAL_CHECK_THRESHOLD) {
            onGameFinishListener?.invoke(frameTime.startSeconds - mTimeMeasurementStart)
        }
    }

    fun beginTimeMeasurement() {
        mBeginTimeMeasurement = true
    }
}
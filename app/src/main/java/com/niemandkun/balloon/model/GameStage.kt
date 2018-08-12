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
        worldPosition = Vector3.FORWARD.mul(Constants.DISTANCE_TO_PORTAL).add(Vector3.UP.mul(0.5f)).toSceneform()
    }

    private val speedometer = Speedometer(speedometerRenderable).apply {
        localPosition = Vector3.UP.mul(0.6f).toSceneform()
    }

    init {
        balloon.addChild(speedometer)
        addChild(balloon)
        addChild(portal)
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        val balloonToPortalDistance = Vector3.fromSceneform(balloon.worldPosition).keepXZ()
                .sub(Vector3.fromSceneform(portal.worldPosition).keepXZ())
                .length()

        if (balloonToPortalDistance < Constants.BALLOON_TO_PORTAL_CHECK_THRESHOLD) {
            removeChild(portal)
        }
    }
}
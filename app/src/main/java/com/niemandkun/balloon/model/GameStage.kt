package com.niemandkun.balloon.model

import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Renderable
import com.niemandkun.balloon.math.Vector3

class GameStage(balloonRenderable: Renderable, portalRenderable: Renderable) : Node() {
    val balloon = GameObject(Constants.BALLOON_MASS, Constants.AIR_FRICTION, balloonRenderable).apply {
        worldPosition = Vector3.UP.toSceneform()
    }

    val portal = GameObject(Constants.PORTAL_MASS, Constants.AIR_FRICTION, portalRenderable)

    init {
        addChild(balloon)
        // TODO
        // addChild(portal)
    }
}
package com.niemandkun.balloon.model

import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.Renderable
import com.niemandkun.balloon.math.Vector3

open class GameObject(mass: Float, friction: Float, renderable: Renderable)  : Node() {
    private var mVelocity = Vector3.ZERO

    private var mAcceleration = Vector3.ZERO

    private val mFriction = friction

    private val mMass = mass

    init {
        this.renderable = renderable
    }

    override fun onUpdate(frameTime: FrameTime) {
        super.onUpdate(frameTime)
        localPosition = Vector3.fromSceneform(localPosition).add(mVelocity).toSceneform()
        mVelocity = mVelocity.add(mAcceleration).mul(1 - mFriction)
        mAcceleration = Vector3.ZERO
    }

    fun applyForce(force: Vector3) {
        mAcceleration = mAcceleration.add(force.div(mMass))
    }
}

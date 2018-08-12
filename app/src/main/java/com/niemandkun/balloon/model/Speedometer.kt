package com.niemandkun.balloon.model

import android.widget.TextView
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.rendering.ViewRenderable
import timber.log.Timber
import com.niemandkun.balloon.R

class Speedometer(renderable: ViewRenderable) : Node() {
    private val mTextView = renderable.view as TextView

    init {
        this.renderable = renderable
    }

    override fun onUpdate(frameTime: FrameTime) {
        super.onUpdate(frameTime)
        if (parent !is GameObject) {
            Timber.e("Speedometer is attached to Node that is not GameObject. Skipping update.")
            return
        }
        mTextView.text = mTextView.resources
                .getString(R.string.centimeters_per_second, (parent as GameObject).velocity.length() * 100)
    }
}

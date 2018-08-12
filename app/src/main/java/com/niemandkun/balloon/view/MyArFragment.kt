package com.niemandkun.balloon.view

import android.Manifest
import com.google.ar.core.*
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.ux.ArFragment

class MyArFragment : ArFragment() {
    private var mOnAugmentedImageFoundListener: ((Collection<AugmentedImage>) -> Unit)? = null

    override fun getAdditionalPermissions(): Array<String> {
        return arrayOf(Manifest.permission.RECORD_AUDIO)
    }

    override fun getSessionConfiguration(session: Session?): Config {
        val config = super.getSessionConfiguration(session)
        val inputStream = context!!.assets.open("images.imgdb")
        config.augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, inputStream)
        return config
    }

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)
        val frame = arSceneView.arFrame ?: return
        val images = frame.getUpdatedTrackables(AugmentedImage::class.java)
                .filter { it.trackingState == TrackingState.TRACKING }

        mOnAugmentedImageFoundListener.takeIf { images.isNotEmpty() }?.apply {
            invoke(images)
        }
    }

    fun setOnAugmentedImageFoundListener(listener: ((Collection<AugmentedImage>) -> Unit)?) {
        mOnAugmentedImageFoundListener = listener
    }
}

package com.niemandkun.balloon

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var mBalloon: Renderable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        val arFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment) as ArFragment

        ModelRenderable.builder()
                .setSource(this, R.raw.balloon)
                .build()
                .thenAccept { mBalloon = it }
                .exceptionally { e -> Timber.e(e); null }

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            Timber.e("hit!")
            if (mBalloon != null) {
                Timber.e("Balloon is available")
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.renderable = mBalloon
                anchorNode.setParent(arFragment.arSceneView.scene);
            }
        }
    }
}

package com.niemandkun.balloon.model

import android.app.Activity
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.niemandkun.balloon.R
import java.util.concurrent.CompletableFuture

object GameStageFactory {
    fun createScene(context: Activity): CompletableFuture<GameStage> {
        val balloonRenderable = ModelRenderable.builder().setSource(context, R.raw.balloon).build()

        val portalRenderable = ModelRenderable.builder().setSource(context, R.raw.portal).build()

        val speedometerRenderable = ViewRenderable.builder().setView(context, R.layout.view_speedometer).build()

        return CompletableFuture.allOf(balloonRenderable, portalRenderable, speedometerRenderable)
                .thenApply { GameStage(balloonRenderable.get(), portalRenderable.get(), speedometerRenderable.get()) }
    }
}
package com.niemandkun.balloon.model

import android.content.Context
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture
import com.niemandkun.balloon.R

object GameStageFactory {
    fun createScene(context: Context): CompletableFuture<GameStage> {
        val balloonRenderable = ModelRenderable.builder().setSource(context, R.raw.balloon).build()

        val portalRenderable = ModelRenderable.builder().setSource(context, R.raw.portal).build()

        return CompletableFuture.allOf(balloonRenderable, portalRenderable)
                .thenApply { GameStage(balloonRenderable.get(), portalRenderable.get()) }
    }
}
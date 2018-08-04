package com.niemandkun.balloon.util

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder

class SoundMeter {
    private var mAudioRecord: AudioRecord? = null
    private var mMinBufferSize: Int = 0

    fun start() {
        mMinBufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)

        mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mMinBufferSize)

        mAudioRecord!!.startRecording()
    }

    fun stop() {
        if (mAudioRecord != null) {
            mAudioRecord!!.stop()
        }
    }

    fun getAmplitude(): Float {
        if (mAudioRecord == null) {
            return 0f
        }
        val buffer = ShortArray(mMinBufferSize)
        mAudioRecord!!.read(buffer, 0, mMinBufferSize)
        return (buffer.max() ?: 0).toFloat()
    }
}

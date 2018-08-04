package com.niemandkun.balloon.util

import com.google.ar.core.Pose
import com.niemandkun.balloon.math.Vector3

fun Pose.getViewDirection(): Vector3 {
    return Vector3.fromFloatArray(this.rotateVector(Vector3.FORWARD.toFloatArray()))
}
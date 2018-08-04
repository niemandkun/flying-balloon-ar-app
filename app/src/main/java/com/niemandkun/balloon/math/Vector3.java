/*
 * Copyright (C) 2017 Poroshin Ivan
 * This file is part of adddxdx.
 *
 * adddxdx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * adddxdx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with adddxdx.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.niemandkun.balloon.math;

import android.support.annotation.NonNull;

public class Vector3 implements Vector<Vector3> {
    public static final Vector3 ORT_X = new Vector3(1, 0, 0);
    public static final Vector3 ORT_Y = new Vector3(0, 1, 0);
    public static final Vector3 ORT_Z = new Vector3(0, 0, 1);
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    public static final Vector3 ONE = new Vector3(1, 1, 1);

    public static final Vector3 RIGHT = ORT_X;
    public static final Vector3 LEFT = RIGHT.negate();

    public static final Vector3 UP = ORT_Y;
    public static final Vector3 DOWN = UP.negate();

    public static final Vector3 BACKWARD = ORT_Z;
    public static final Vector3 FORWARD = BACKWARD.negate();

    private final float x;
    private final float y;
    private final float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static @NonNull
    Vector3 fromFloatArray(@NonNull float[] vector) {
        return new Vector3(vector[0], vector[1], vector[2]);
    }

    public static @NonNull
    Vector3 fromSceneform(@NonNull com.google.ar.sceneform.math.Vector3 vector) {
        return new Vector3(vector.x, vector.y, vector.z);
    }

    @NonNull
    public com.google.ar.sceneform.math.Vector3 toSceneform() {
        return new com.google.ar.sceneform.math.Vector3(x, y, z);
    }

    @Override
    public @NonNull
    float[] toFloatArray() {
        return new float[]{x, y, z};
    }

    public @NonNull
    void toFloatArray(float[] outArray) {
        outArray[0] = x;
        outArray[1] = y;
        outArray[2] = z;
    }

    @Override
    public @NonNull
    Vector3 mul(float k) {
        return new Vector3(x * k, y * k, z * k);
    }

    @Override
    public @NonNull
    Vector3 div(float k) {
        return new Vector3(x / k, y / k, z / k);
    }

    @Override
    public @NonNull
    Vector3 sub(@NonNull Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    @Override
    public @NonNull
    Vector3 add(@NonNull Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    @Override
    public float dot(@NonNull Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3 cross(@NonNull Vector3 other) {
        return new Vector3(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public float distanceTo(@NonNull Vector3 other) {
        return this.sub(other).length();
    }

    public @NonNull
    Vector4 toVec4(float w) {
        return new Vector4(x, y, z, w);
    }

    public @NonNull
    Vector4 toVec4() {
        return new Vector4(x, y, z, 0);
    }

    public @NonNull
    Vector2 clipToVec2() {
        return new Vector2(x, y);
    }

    public @NonNull
    Vector3 rotateAroundOx(float angle) {
        return Quaternion.fromEulerAngles(angle, 0, 0).apply(this);
    }

    public @NonNull
    Vector3 rotateAroundOy(float angle) {
        return Quaternion.fromEulerAngles(0, angle, 0).apply(this);
    }

    public @NonNull
    Vector3 rotateAroundOz(float angle) {
        return Quaternion.fromEulerAngles(0, 0, angle).apply(this);
    }

    public @NonNull
    Vector3 rotate(float x, float y, float z) {
        return Quaternion.fromEulerAngles(x, y, z).apply(this);
    }

    public @NonNull
    Vector3 rotate(Vector3 eulerAngles) {
        return Quaternion.fromEulerAngles(eulerAngles).apply(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Vector3 vector3 = (Vector3) other;

        return Float.compare(vector3.x, x) == 0
                && Float.compare(vector3.y, y) == 0
                && Float.compare(vector3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public float getX() {
        return x;
    }

    public Vector3 setX(float x) {
        return new Vector3(x, y, z);
    }

    public float getY() {
        return y;
    }

    public Vector3 setY(float y) {
        return new Vector3(x, y, z);
    }

    public float getZ() {
        return z;
    }

    public Vector3 setZ(float z) {
        return new Vector3(x, y, z);
    }
}

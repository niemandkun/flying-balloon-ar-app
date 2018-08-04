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

import java.util.Arrays;

public class Vector4 implements Vector<Vector4> {
    public static final Vector4 ORT_X = new Vector4(1, 0, 0, 0);
    public static final Vector4 ORT_Y = new Vector4(0, 1, 0, 0);
    public static final Vector4 ORT_Z = new Vector4(0, 0, 1, 0);
    public static final Vector4 ORT_W = new Vector4(0, 0, 0, 1);
    public static final Vector4 ZERO = new Vector4(0, 0, 0, 0);
    public static final Vector4 ONE = new Vector4(1, 1, 1, 1);

    public static final Vector4 LEFT = ORT_X;
    public static final Vector4 RIGHT = LEFT.negate();

    public static final Vector4 UP = ORT_Y;
    public static final Vector4 DOWN = UP.negate();

    public static final Vector4 FORWARD = ORT_Z;
    public static final Vector4 BACKWARD = FORWARD.negate();

    private final float[] mVector;

    public Vector4(float x, float y, float z, float w) {
        this.mVector = new float[]{x, y, z, w};
    }

    public Vector4(@NonNull float[] vector) {
        if (vector.length != 4) throw new IllegalArgumentException();
        this.mVector = vector;
    }

    public static @NonNull
    Vector4 fromFloatArray(@NonNull float[] vector) {
        return new Vector4(vector[0], vector[1], vector[2], vector[3]);
    }

    @Override
    public @NonNull
    float[] toFloatArray() {
        return Arrays.copyOf(mVector, mVector.length);
    }

    public @NonNull
    Vector4 homogenize() {
        return new Vector4(new float[]{
                mVector[0] / mVector[3],
                mVector[1] / mVector[3],
                mVector[2] / mVector[3],
                1
        });
    }

    @Override
    public @NonNull
    Vector4 div(float value) {
        return new Vector4(new float[]{
                mVector[0] / value,
                mVector[1] / value,
                mVector[2] / value,
                mVector[3] / value
        });
    }

    @Override
    public @NonNull
    Vector4 mul(float value) {
        return new Vector4(new float[]{
                mVector[0] * value,
                mVector[1] * value,
                mVector[2] * value,
                mVector[3] * value
        });
    }

    @Override
    public @NonNull
    Vector4 add(@NonNull Vector4 other) {
        return new Vector4(new float[]{
                this.mVector[0] + other.mVector[0],
                this.mVector[1] + other.mVector[1],
                this.mVector[2] + other.mVector[2],
                this.mVector[3] + other.mVector[3],
        });
    }

    @Override
    public @NonNull
    Vector4 sub(@NonNull Vector4 other) {
        return new Vector4(new float[]{
                this.mVector[0] - other.mVector[0],
                this.mVector[1] - other.mVector[1],
                this.mVector[2] - other.mVector[2],
                this.mVector[3] - other.mVector[3],
        });
    }

    @Override
    public float dot(@NonNull Vector4 other) {
        return 0;
    }

    public @NonNull
    Vector3 clipToVec3() {
        return new Vector3(mVector[0], mVector[1], mVector[2]);
    }

    public @NonNull
    Vector2 clipToVec2() {
        return new Vector2(mVector[0], mVector[1]);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Vector4 vector4 = (Vector4) other;

        return Arrays.equals(mVector, vector4.mVector);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mVector);
    }

    @Override
    public String toString() {
        return "(" + mVector[0] + ", "
                + mVector[1] + ", "
                + mVector[2] + ", "
                + mVector[3] + ")";
    }

    public float getX() {
        return mVector[0];
    }

    public Vector4 setX(float x) {
        return new Vector4(new float[]{x, mVector[1], mVector[2], mVector[3]});
    }

    public float getY() {
        return mVector[1];
    }

    public Vector4 setY(float y) {
        return new Vector4(new float[]{mVector[0], y, mVector[2], mVector[3]});
    }

    public float getZ() {
        return mVector[2];
    }

    public Vector4 setZ(float z) {
        return new Vector4(new float[]{mVector[0], mVector[1], z, mVector[3]});
    }

    public float getW() {
        return mVector[3];
    }

    public Vector4 setW(float w) {
        return new Vector4(new float[]{mVector[0], mVector[1], mVector[2], w});
    }
}

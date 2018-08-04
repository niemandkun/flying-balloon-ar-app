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

public class Matrix4 {
    private final static int ROW_LENGTH = 4;
    private final static int COLUMN_LENGTH = 4;
    private final static int ELEMENTS_COUNT = ROW_LENGTH * COLUMN_LENGTH;

    private final float[] mMatrix;

    public Matrix4(
            float a11, float a12, float a13, float a14,
            float a21, float a22, float a23, float a24,
            float a31, float a32, float a33, float a34,
            float a41, float a42, float a43, float a44) {

        mMatrix = new float[]{
                a11, a12, a13, a14,
                a21, a22, a23, a24,
                a31, a32, a33, a34,
                a41, a42, a43, a44,
        };
    }

    private Matrix4(@NonNull float[] matrix) {
        mMatrix = matrix;
    }

    public @NonNull
    float[] toFloatArray() {
        return Arrays.copyOf(mMatrix, mMatrix.length);
    }

    public @NonNull
    Matrix4 cross(@NonNull Matrix4 other) {
        return new Matrix4(fastMatrixMultiply(this.mMatrix, other.mMatrix));
    }

    private static float[] fastMatrixMultiply(@NonNull float[] a, @NonNull float[] b) {
        float[] mat = new float[ELEMENTS_COUNT];

        for (int i = 0; i < ELEMENTS_COUNT; i += ROW_LENGTH)
            for (int j = 0; j < COLUMN_LENGTH; ++j)
                mat[i + j] =
                        a[i] * b[j]
                                + a[i + 1] * b[4 + j]
                                + a[i + 2] * b[8 + j]
                                + a[i + 3] * b[12 + j];

        return mat;
    }

    public @NonNull
    Vector4 cross(@NonNull Vector4 vector) {
        return new Vector4(fastVectorMultiply(this.mMatrix, vector.toFloatArray()));
    }

    private static @NonNull
    float[] fastVectorMultiply(@NonNull float[] a, @NonNull float[] v) {
        float[] vec = new float[COLUMN_LENGTH];

        vec[0] = a[0] * v[0] + a[1] * v[1] + a[2] * v[2] + a[3] * v[3];
        vec[1] = a[4] * v[0] + a[5] * v[1] + a[6] * v[2] + a[7] * v[3];
        vec[2] = a[8] * v[0] + a[9] * v[1] + a[10] * v[2] + a[11] * v[3];
        vec[3] = a[12] * v[0] + a[13] * v[1] + a[14] * v[2] + a[15] * v[3];

        return vec;
    }

    public @NonNull
    Matrix4 inverse() {
        return new Matrix4(fastMatrixInverse(mMatrix));
    }

    private static @NonNull
    float[] fastMatrixInverse(@NonNull float[] m) {
        float[] inv = new float[ELEMENTS_COUNT];

        inv[0] = m[5] * m[10] * m[15] - m[5] * m[11] * m[14] - m[9] * m[6] * m[15] + m[9] * m[7] * m[14] + m[13] * m[6] * m[11] - m[13] * m[7] * m[10];
        inv[4] = -m[4] * m[10] * m[15] + m[4] * m[11] * m[14] + m[8] * m[6] * m[15] - m[8] * m[7] * m[14] - m[12] * m[6] * m[11] + m[12] * m[7] * m[10];
        inv[8] = m[4] * m[9] * m[15] - m[4] * m[11] * m[13] - m[8] * m[5] * m[15] + m[8] * m[7] * m[13] + m[12] * m[5] * m[11] - m[12] * m[7] * m[9];
        inv[12] = -m[4] * m[9] * m[14] + m[4] * m[10] * m[13] + m[8] * m[5] * m[14] - m[8] * m[6] * m[13] - m[12] * m[5] * m[10] + m[12] * m[6] * m[9];

        inv[1] = -m[1] * m[10] * m[15] + m[1] * m[11] * m[14] + m[9] * m[2] * m[15] - m[9] * m[3] * m[14] - m[13] * m[2] * m[11] + m[13] * m[3] * m[10];
        inv[5] = m[0] * m[10] * m[15] - m[0] * m[11] * m[14] - m[8] * m[2] * m[15] + m[8] * m[3] * m[14] + m[12] * m[2] * m[11] - m[12] * m[3] * m[10];
        inv[9] = -m[0] * m[9] * m[15] + m[0] * m[11] * m[13] + m[8] * m[1] * m[15] - m[8] * m[3] * m[13] - m[12] * m[1] * m[11] + m[12] * m[3] * m[9];
        inv[13] = m[0] * m[9] * m[14] - m[0] * m[10] * m[13] - m[8] * m[1] * m[14] + m[8] * m[2] * m[13] + m[12] * m[1] * m[10] - m[12] * m[2] * m[9];

        inv[2] = m[1] * m[6] * m[15] - m[1] * m[7] * m[14] - m[5] * m[2] * m[15] + m[5] * m[3] * m[14] + m[13] * m[2] * m[7] - m[13] * m[3] * m[6];
        inv[6] = -m[0] * m[6] * m[15] + m[0] * m[7] * m[14] + m[4] * m[2] * m[15] - m[4] * m[3] * m[14] - m[12] * m[2] * m[7] + m[12] * m[3] * m[6];
        inv[10] = m[0] * m[5] * m[15] - m[0] * m[7] * m[13] - m[4] * m[1] * m[15] + m[4] * m[3] * m[13] + m[12] * m[1] * m[7] - m[12] * m[3] * m[5];
        inv[14] = -m[0] * m[5] * m[14] + m[0] * m[6] * m[13] + m[4] * m[1] * m[14] - m[4] * m[2] * m[13] - m[12] * m[1] * m[6] + m[12] * m[2] * m[5];

        inv[3] = -m[1] * m[6] * m[11] + m[1] * m[7] * m[10] + m[5] * m[2] * m[11] - m[5] * m[3] * m[10] - m[9] * m[2] * m[7] + m[9] * m[3] * m[6];
        inv[7] = m[0] * m[6] * m[11] - m[0] * m[7] * m[10] - m[4] * m[2] * m[11] + m[4] * m[3] * m[10] + m[8] * m[2] * m[7] - m[8] * m[3] * m[6];
        inv[11] = -m[0] * m[5] * m[11] + m[0] * m[7] * m[9] + m[4] * m[1] * m[11] - m[4] * m[3] * m[9] - m[8] * m[1] * m[7] + m[8] * m[3] * m[5];
        inv[15] = m[0] * m[5] * m[10] - m[0] * m[6] * m[9] - m[4] * m[1] * m[10] + m[4] * m[2] * m[9] + m[8] * m[1] * m[6] - m[8] * m[2] * m[5];

        float det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];

        if (det == 0) throw new IllegalArgumentException("Matrix has no inverse (determinant is zero).");

        det = 1 / det;

        for (int i = 0; i < inv.length; i++)
            inv[i] *= det;

        return inv;
    }

    public @NonNull
    Matrix4 sub(@NonNull Matrix4 other) {
        float[] diff = new float[ELEMENTS_COUNT];

        for (int i = 0; i < ELEMENTS_COUNT; ++i)
            diff[i] = mMatrix[i] - other.mMatrix[i];

        return new Matrix4(diff);
    }

    public @NonNull
    Matrix4 add(@NonNull Matrix4 other) {
        float[] sum = new float[ELEMENTS_COUNT];

        for (int i = 0; i < ELEMENTS_COUNT; ++i)
            sum[i] = mMatrix[i] + other.mMatrix[i];

        return new Matrix4(sum);
    }

    public static @NonNull
    Matrix4 getTranslationMatrix(Vector3 v) {
        return getTranslationMatrix(v.getX(), v.getY(), v.getZ());
    }

    public static @NonNull
    Matrix4 getTranslationMatrix(float x, float y, float z) {
        return new Matrix4(new float[]{
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1,
        });
    }

    public static @NonNull
    Matrix4 getRotationMatrix(Vector3 v) {
        return getRotationMatrix(v.getX(), v.getY(), v.getZ());
    }

    public static @NonNull
    Matrix4 getRotationMatrix(float x, float y, float z) {
        return new Matrix4(new float[]
                {
                        1, 0, 0, 0,
                        0, (float) Math.cos(x), (float) -Math.sin(x), 0,
                        0, (float) Math.sin(x), (float) Math.cos(x), 0,
                        0, 0, 0, 1,
                }
        ).cross(new Matrix4(new float[]
                {
                        (float) Math.cos(y), 0, (float) Math.sin(y), 0,
                        0, 1, 0, 0,
                        (float) -Math.sin(y), 0, (float) Math.cos(y), 0,
                        0, 0, 0, 1,
                }
        )).cross(new Matrix4(new float[]
                {
                        (float) Math.cos(z), (float) -Math.sin(z), 0, 0,
                        (float) Math.sin(z), (float) Math.cos(z), 0, 0,
                        0, 0, 1, 0,
                        0, 0, 0, 1,
                }
        ));
    }

    public static @NonNull
    Matrix4 getScaleMatrix(Vector3 v) {
        return getScaleMatrix(v.getX(), v.getY(), v.getZ());
    }

    public static @NonNull
    Matrix4 getScaleMatrix(float x, float y, float z) {
        return new Matrix4(new float[]{
                x, 0, 0, 0,
                0, y, 0, 0,
                0, 0, z, 0,
                0, 0, 0, 1,
        });
    }

    public static final @NonNull
    Matrix4 IDENTITY = new Matrix4(new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    });

    public static final @NonNull
    Matrix4 ZERO = new Matrix4(new float[]{
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
    });
}

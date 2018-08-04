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

public class Vector2 implements Vector<Vector2> {
    public static final Vector2 ORT_X = new Vector2(1, 0);
    public static final Vector2 ORT_Y = new Vector2(0, 1);
    public static final Vector2 ZERO = new Vector2(0, 0);
    public static final Vector2 ONE = new Vector2(1, 1);

    public static final Vector2 LEFT = ORT_X;
    public static final Vector2 RIGHT = LEFT.negate();

    public static final Vector2 UP = ORT_Y;
    public static final Vector2 DOWN = UP.negate();

    private float x;
    private float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public @NonNull
    float[] toFloatArray() {
        return new float[]{x, y};
    }

    public void toFloatArray(float[] outArray) {
        outArray[0] = x;
        outArray[1] = y;
    }

    public static @NonNull
    Vector2 fromFloatArray(@NonNull float[] vector) {
        return new Vector2(vector[0], vector[1]);
    }

    public float chessboardDistance(@NonNull Vector2 other) {
        return Math.max(Math.abs(x - other.x), Math.abs(y - other.y));
    }

    public @NonNull
    Vector2 getNormal() {
        return new Vector2(-y, x);
    }

    public @NonNull
    Vector2 rotate(float angle) {
        return new Vector2(
                x * (float) Math.cos(angle) + y * (float) Math.sin(angle),
                -x * (float) Math.sin(angle) + y * (float) Math.cos(angle)
        );
    }

    @Override
    public @NonNull
    Vector2 add(@NonNull Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    @Override
    public @NonNull
    Vector2 sub(@NonNull Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    @Override
    public @NonNull
    Vector2 mul(float k) {
        return new Vector2(x * k, y * k);
    }

    @Override
    public @NonNull
    Vector2 div(float k) {
        return new Vector2(x / k, y / k);
    }

    @Override
    public float dot(@NonNull Vector2 other) {
        return x * other.x + y * other.y;
    }

    public float cross(@NonNull Vector2 other) {
        return x * other.y - y * other.x;
    }

    public float angle() {
        return (float) Math.atan2(y, x);
    }

    public @NonNull
    Vector4 toVec4(float z, float w) {
        return new Vector4(x, y, z, w);
    }

    public @NonNull
    Vector4 toVec4() {
        return new Vector4(x, y, 0, 0);
    }

    public @NonNull
    Vector3 toVec3(float z) {
        return new Vector3(x, y, z);
    }

    public @NonNull
    Vector3 toVec3() {
        return new Vector3(x, y, 0);
    }

    public int getOctant() {
        if (y > 0) {
            if (x > 0) {
                if (x > y) return 0;
                return 1;
            } else {
                if (y * y > x * x) return 2;
                return 3;
            }
        } else {
            if (x < 0) {
                if (x < y) return 4;
                return 5;
            } else {
                if (y * y > x * x) return 6;
                return 7;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2 vector2i = (Vector2) o;

        return x == vector2i.x && y == vector2i.y;
    }

    @Override
    public int hashCode() {
        return 1023 * Float.hashCode(x) + Float.hashCode(y);
    }

    public String toString() {
        return String.format("(%f, %f)", x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2 setX(float x) {
        return new Vector2(x, y);
    }

    public Vector2 setY(float y) {
        return new Vector2(x, y);
    }
}

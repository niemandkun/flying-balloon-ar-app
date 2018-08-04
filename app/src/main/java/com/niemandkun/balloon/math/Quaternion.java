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

public class Quaternion {
    public static final Quaternion IDENTITY = Quaternion.fromEulerAngles(0, 0, 0);

    private float r;
    private float i;
    private float j;
    private float k;

    private Quaternion(float r, float i, float j, float k) {
        this.r = r;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Quaternion dot(Quaternion v) {
        Quaternion u = this;

        return new Quaternion(
                u.r * v.r - u.i * v.i - u.j * v.j - u.k * v.k,
                u.r * v.i + u.i * v.r + u.j * v.k - u.k * v.j,
                u.r * v.j - u.i * v.k + u.j * v.r + u.k * v.i,
                u.r * v.k + u.i * v.j - u.j * v.i + u.k * v.r
        );
    }

    public Vector3 apply(Vector3 vector) {
        Quaternion vec = new Quaternion(0, vector.getX(), vector.getY(), vector.getZ());
        Quaternion rot = dot(vec).dot(inverse());
        return new Vector3(rot.i, rot.j, rot.k);
    }

    public Quaternion rotate(float x, float y, float z) {
        return dot(fromEulerAngles(x, y, z));
    }

    public Quaternion rotate(Vector3 eulerAngles) {
        return dot(fromEulerAngles(eulerAngles));
    }

    public Quaternion inverse() {
        return new Quaternion(r, -i, -j, -k);
    }

    public float norm() {
        return (float) Math.sqrt(1 / (r * r + i * i + j * j + k * k));
    }

    public Matrix4 getRotationMatrix() {
        float s = 1 / (r * r + i * i + j * j + k * k);

        return new Matrix4(
                1 - 2 * s * (j * j + k * k), 2 * s * (i * j - k * r), 2 * s * (i * k + j * r), 0,
                2 * s * (i * j + k * r), 1 - 2 * s * (i * i + k * k), 2 * s * (j * k - i * r), 0,
                2 * s * (i * k - j * r), 2 * s * (j * k + i * r), 1 - 2 * s * (i * i + j * j), 0,
                0, 0, 0, 1
        );
    }

    public static Quaternion fromEulerAngles(Vector3 eulerAngles) {
        return fromEulerAngles(eulerAngles.getX(), eulerAngles.getY(), eulerAngles.getZ());
    }

    public static Quaternion fromFloatArray(float[] components) {
        if (components.length != 4) {
            throw new IllegalArgumentException("Expected 4 component vector for Quaternion");
        }
        return new Quaternion(components[0], components[1], components[2], components[3]);
    }

    public static Quaternion fromEulerAngles(float x, float y, float z) {
        double cosZ = Math.cos(z * 0.5);
        double sinZ = Math.sin(z * 0.5);
        double cosX = Math.cos(x * 0.5);
        double sinX = Math.sin(x * 0.5);
        double cosY = Math.cos(y * 0.5);
        double sinY = Math.sin(y * 0.5);

        double r = cosZ * cosX * cosY + sinZ * sinX * sinY;
        double i = cosZ * sinX * cosY - sinZ * cosX * sinY;
        double j = cosZ * cosX * sinY + sinZ * sinX * cosY;
        double k = sinZ * cosX * cosY - cosZ * sinX * sinY;

        return new Quaternion((float) r, (float) i, (float) j, (float) k);
    }

    public Vector3 toEulerAngles() {
        double t0 = 2 * (r * i + j * k);
        double t1 = 1 - 2 * (i * i + j * j);
        double x = Math.atan2(t0, t1);

        double t2 = 2 * (r * j - k * i);
        t2 = t2 > 1 ? 1 : t2;
        t2 = t2 < -1 ? -1 : t2;
        double y = Math.asin(t2);

        double t3 = 2 * (r * k + i * j);
        double t4 = 1 - 2 * (j * j + k * k);
        double z = Math.atan2(t3, t4);

        return new Vector3((float) x, (float) y, (float) z);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Quaternion that = (Quaternion) other;

        return that.r == r && that.i == i && that.j == j && that.k == k;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(r);
        result = 31 * result + Float.hashCode(i);
        result = 31 * result + Float.hashCode(j);
        result = 31 * result + Float.hashCode(k);
        return result;
    }

    @Override
    public String toString() {
        return toEulerAngles().toString();
    }
}

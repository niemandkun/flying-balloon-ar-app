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

public interface Vector<TVector extends Vector<TVector>> {
    @NonNull
    TVector add(@NonNull TVector other);

    @NonNull
    TVector sub(@NonNull TVector other);

    @NonNull
    TVector mul(float k);

    @NonNull
    TVector div(float k);

    float dot(@NonNull TVector other);

    @NonNull
    float[] toFloatArray();

    default float length() {
        return (float) Math.sqrt(length2());
    }

    default float length2() {
        return dot((TVector) this);
    }

    default float distance(TVector other) {
        return (float) Math.sqrt(distance2(other));
    }

    default float distance2(TVector other) {
        return sub(other).length();
    }

    default float angle(TVector other) {
        return (float) Math.acos(this.dot(other) / this.length() / other.length());
    }

    default @NonNull
    TVector normalize() {
        return div(length());
    }

    default @NonNull
    TVector negate() {
        return mul(-1);
    }
}

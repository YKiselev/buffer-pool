/*
 * Copyright 2017 Yuriy Kiselev (uze@yandex.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ykiselev.buffers.align;

/**
 * Aligns supplied value tn next multiple of {@code base}
 * <p>
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class MultipleOfAdjustment implements IntAdjustment {

    private final int base;

    public MultipleOfAdjustment(int base) {
        this.base = base;
    }

    @Override
    public int apply(int value) {
        final int aligned = this.base * (value / this.base);
        if (aligned < value) {
            value = aligned + this.base;
        } else {
            value = aligned;
        }
        return value;
    }
}

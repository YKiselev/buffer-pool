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
 * Rounds supplied value to the next anchor or leaves as is if no anchor found
 * <p>
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class AnchorAdjustment implements IntAdjustment {

    private final int[] anchors;

    /**
     * @param anchors the sizes to snap to (array should be sorted in ascending order, i.e. {16, 32, 64})
     */
    public AnchorAdjustment(int... anchors) {
        this.anchors = anchors.clone();
    }

    @Override
    public int apply(int value) {
        for (int anchor : anchors) {
            if (value < anchor) {
                value = anchor;
                break;
            }
        }
        return value;
    }
}

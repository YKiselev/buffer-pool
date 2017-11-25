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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public class AnchorAdjustmentTest {

    private final AnchorAdjustment function = new AnchorAdjustment(
            5, 10, 15, 25, 50
    );

    @Test
    public void shouldAlignToAnchors() throws Exception {
        assertEquals(5, function.apply(-100));
        assertEquals(5, function.apply(0));
        assertEquals(5, function.apply(3));
        assertEquals(10, function.apply(7));
        assertEquals(15, function.apply(11));
        assertEquals(25, function.apply(16));
        assertEquals(50, function.apply(26));
    }

    @Test
    public void shouldRemainTheSame() throws Exception {
        assertEquals(51, function.apply(51));
        assertEquals(100, function.apply(100));
    }

}
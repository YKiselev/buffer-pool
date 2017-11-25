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

import com.github.ykiselev.buffers.align.PowerOfTwoAlignFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public class PowerOfTwoAlignFunctionTest {

    private final PowerOfTwoAlignFunction fn = new PowerOfTwoAlignFunction();

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNegative() throws Exception {
        fn.apply(-1);
    }

    @Test
    public void shouldWork() throws Exception {
        assertEquals(0, fn.apply(0));
        assertEquals(1, fn.apply(1));
        assertEquals(2, fn.apply(2));
        assertEquals(4, fn.apply(3));
        assertEquals(16, fn.apply(9));
        assertEquals(32, fn.apply(17));
        assertEquals(64, fn.apply(51));
        assertEquals(128, fn.apply(100));
        assertEquals(256, fn.apply(200));
        assertEquals(512, fn.apply(477));
    }

}
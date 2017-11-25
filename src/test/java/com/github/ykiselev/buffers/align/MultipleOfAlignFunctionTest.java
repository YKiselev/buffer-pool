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

import com.github.ykiselev.buffers.align.MultipleOfAlignFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public class MultipleOfAlignFunctionTest {

    private final MultipleOfAlignFunction function = new MultipleOfAlignFunction(10);

    @Test
    public void shouldBeMultipleOfTen() throws Exception {
        assertEquals(10, function.apply(1));
        assertEquals(10, function.apply(3));
        assertEquals(20, function.apply(11));
        assertEquals(30, function.apply(22));
    }
}
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

package com.github.ykiselev.buffers.factories;

import com.github.ykiselev.buffers.factories.BufferFactory;
import com.github.ykiselev.buffers.factories.SizeAdjustingBufferFactory;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public class SizeAdjustingBufferFactoryTest {

    private final BufferFactory<ByteBuffer> factory = new SizeAdjustingBufferFactory<>(
            ByteBuffer::allocate,
            value -> {
                if (value <= 128) {
                    return 128;
                }
                throw new IllegalArgumentException();
            }
    );

    @Test
    public void shouldAdjust() throws Exception {
        final ByteBuffer buffer = factory.create(1);
        assertEquals(128, buffer.capacity());
    }
}
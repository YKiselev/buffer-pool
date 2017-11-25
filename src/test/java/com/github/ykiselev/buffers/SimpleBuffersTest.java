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

package com.github.ykiselev.buffers;

import com.github.ykiselev.buffers.pool.BufferPool;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public class SimpleBuffersTest {

    private final ByteBuffer buffer = ByteBuffer.allocate(16);

    private final Buffers buffers = new SimpleBuffers(
            new BufferPool<ByteBuffer>() {
                @Override
                public ByteBuffer acquire(int size) {
                    if (size > buffer.capacity()) {
                        throw new IllegalArgumentException("Out of memory!");
                    }
                    return buffer;
                }

                @Override
                public void release(ByteBuffer b) {
                    assertEquals(buffer, b);
                }
            }
    );

    @Test
    public void shouldAllocateByteBuffer() throws Exception {
        try (Buffers.Pooled<ByteBuffer> pb = buffers.byteBuffer(10)) {
            final ByteBuffer buffer = pb.buffer();
            assertEquals(0, buffer.position());
            assertEquals(buffer.limit(), buffer.capacity());
            assertTrue(buffer.capacity() >= 10);
        }
    }

    @Test
    public void shouldAllocateCharBuffer() throws Exception {
        try (Buffers.Pooled<CharBuffer> pb = buffers.charBuffer(8)) {
            assertTrue(pb.buffer().capacity() >= 8);
        }
    }

    @Test
    public void shouldAllocateShortBuffer() throws Exception {
        try (Buffers.Pooled<ShortBuffer> pb = buffers.shortBuffer(8)) {
            assertTrue(pb.buffer().capacity() >= 8);
        }
    }

    @Test
    public void shouldAllocateIntBuffer() throws Exception {
        try (Buffers.Pooled<IntBuffer> pb = buffers.intBuffer(4)) {
            assertTrue(pb.buffer().capacity() >= 4);
        }
    }

    @Test
    public void shouldAllocateLongBuffer() throws Exception {
        try (Buffers.Pooled<LongBuffer> pb = buffers.longBuffer(2)) {
            assertTrue(pb.buffer().capacity() >= 2);
        }
    }

    @Test
    public void shouldAllocateFloatBuffer() throws Exception {
        try (Buffers.Pooled<FloatBuffer> pb = buffers.floatBuffer(4)) {
            assertTrue(pb.buffer().capacity() >= 4);
        }
    }

    @Test
    public void shouldAllocateDoubleBuffer() throws Exception {
        try (Buffers.Pooled<DoubleBuffer> pb = buffers.doubleBuffer(2)) {
            assertTrue(pb.buffer().capacity() >= 2);
        }
    }

}
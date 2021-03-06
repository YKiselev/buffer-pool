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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import static java.util.Objects.requireNonNull;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class SimpleBuffers implements Buffers {

    private final BufferPool<ByteBuffer> pool;

    public SimpleBuffers(BufferPool<ByteBuffer> pool) {
        this.pool = requireNonNull(pool);
    }

    @Override
    public Pooled<ByteBuffer> byteBuffer(int size) {
        final ByteBuffer buffer = acquire(size);
        return createPooled(buffer, buffer);
    }

    @Override
    public Pooled<FloatBuffer> floatBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Float.BYTES);
        return createPooled(buffer, buffer.asFloatBuffer());
    }

    @Override
    public Pooled<IntBuffer> intBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Integer.BYTES);
        return createPooled(buffer, buffer.asIntBuffer());
    }

    @Override
    public Pooled<CharBuffer> charBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Character.BYTES);
        return createPooled(buffer, buffer.asCharBuffer());
    }

    @Override
    public Pooled<ShortBuffer> shortBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Short.BYTES);
        return createPooled(buffer, buffer.asShortBuffer());
    }

    @Override
    public Pooled<LongBuffer> longBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Long.BYTES);
        return createPooled(buffer, buffer.asLongBuffer());
    }

    @Override
    public Pooled<DoubleBuffer> doubleBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Double.BYTES);
        return createPooled(buffer, buffer.asDoubleBuffer());
    }

    private ByteBuffer acquire(int size) {
        final ByteBuffer result = pool.acquire(size);
        if (result == null) {
            throw new IllegalStateException("Unable to acquire a buffer of size " + size);
        }
        result.clear();
        return result;
    }

    private <T extends Buffer> Pooled<T> createPooled(final ByteBuffer pooled, final T buffer) {
        return new Pooled<T>() {

            @Override
            public T buffer() {
                return buffer;
            }

            @Override
            public void close() {
                pool.release(pooled);
            }

        };
    }
}

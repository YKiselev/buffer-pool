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

package buffers;

import com.google.common.base.Throwables;
import org.uze.terra.editor.domain.Buffers;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.TimeoutException;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class SimpleBuffers implements Buffers {

    private final BufferPool<ByteBuffer> pool;

    public SimpleBuffers(BufferPool<ByteBuffer> pool) {
        this.pool = pool;
    }

    @Override
    public PooledBuffer<ByteBuffer> acquireByteBuffer(int size) {
        final ByteBuffer buffer = acquire(size);
        return createPooled(buffer, buffer);
    }

    @Override
    public PooledBuffer<FloatBuffer> acquireFloatBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Float.BYTES);
        return createPooled(buffer, buffer.asFloatBuffer());
    }

    @Override
    public PooledBuffer<IntBuffer> acquireIntBuffer(int size) {
        final ByteBuffer buffer = acquire(size * Integer.BYTES);
        return createPooled(buffer, buffer.asIntBuffer());
    }

    private ByteBuffer acquire(int size) {
        final ByteBuffer result;
        try {
            result = this.pool.acquire(size, 250);
        } catch (TimeoutException e) {
            throw Throwables.propagate(e);
        }
        result.clear();
        return result;
    }

    private <T extends Buffer> PooledBuffer<T> createPooled(final ByteBuffer pooled, final T buffer) {
        return new PooledBuffer<T>() {

            @Override
            public T buffer() {
                return buffer;
            }

            @Override
            public void close() {
                SimpleBuffers.this.pool.release(pooled);
            }

        };
    }
}

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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public interface Buffers {

    PooledBuffer<ByteBuffer> byteBuffer(int size);

    PooledBuffer<FloatBuffer> floatBuffer(int size);

    PooledBuffer<IntBuffer> intBuffer(int size);

    /**
     * Pooled buffer. Provides access to encapsulated buffer and returns buffer into pool when closed.
     *
     * @param <T> the buffer type
     */
    interface PooledBuffer<T extends Buffer> extends AutoCloseable {

        T buffer();

        @Override
        void close();
    }


}

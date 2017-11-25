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

package com.github.ykiselev.buffers.pool;

import com.github.ykiselev.buffers.factories.BufferFactory;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Simple implementation of {@link BufferPool}.
 * This class is not thread-safe.
 * <p>
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class SimpleBufferPool<T extends Buffer> extends BufferPool<T> {

    private final List<T> pool = new ArrayList<>();

    private final int max;

    private final BufferFactory<T> factory;

    private int counter;

    public SimpleBufferPool(int max, BufferFactory<T> factory) {
        this.max = max;
        this.factory = requireNonNull(factory);
    }

    @Override
    public T acquire(int size) {
        T result = findExisting(size);
        if (result == null) {
            if (counter == max) {
                if (!pool.isEmpty()) {
                    pool.remove(0);
                    counter--;
                }
            }
            if (counter < max) {
                result = factory.create(size);
                counter++;
            }
        }
        return result;
    }

    @Override
    public void release(T buffer) {
        pool.add(buffer);
    }

    private T findExisting(int size) {
        final Iterator<T> it = pool.iterator();
        while (it.hasNext()) {
            final T buffer = it.next();
            if (size <= buffer.capacity()) {
                it.remove();
                return buffer;
            }
        }
        return null;
    }

}

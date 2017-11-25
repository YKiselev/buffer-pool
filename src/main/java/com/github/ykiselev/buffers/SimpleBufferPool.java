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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Simple implementation of {@link BufferPool}.
 * This class is thread-safe.
 * <p>
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class SimpleBufferPool<T extends Buffer> extends BufferPool<T> {

    private final List<T> pool = new ArrayList<>();

    private final int max;

    private final BufferFactory<T> factory;

    private volatile int counter;

    public SimpleBufferPool(int max, BufferFactory<T> factory) {
        this.max = max;
        this.factory = requireNonNull(factory);
    }

    @Override
    public T acquire(int size, int millis) {
        final long deadline = System.currentTimeMillis() + millis;
        do {
            final T result = tryAcquire(size);
            if (result != null) {
                return result;
            }
        } while (System.currentTimeMillis() < deadline);
        return null;
    }

    @Override
    public synchronized void release(T buffer) {
        pool.add(buffer);
    }

    private synchronized T tryAcquire(int aligned) {
        T result = findExisting(aligned);
        if (result == null) {
            if (counter == max) {
                removeSmallest();
            }
            if (counter < max) {
                result = factory.create(aligned);
                counter++;
            }
        }
        return result;
    }

    private void removeSmallest() {
        pool.stream()
                .sorted(Comparator.comparingInt(Buffer::capacity))
                .findFirst()
                .ifPresent(b -> {
                    if (pool.remove(b)) {
                        counter--;
                    }
                });
    }

    private T findExisting(int aligned) {
        final Iterator<T> it = pool.iterator();
        while (it.hasNext()) {
            final T buffer = it.next();
            if (aligned <= buffer.capacity()) {
                it.remove();
                return buffer;
            }
        }
        return null;
    }

}

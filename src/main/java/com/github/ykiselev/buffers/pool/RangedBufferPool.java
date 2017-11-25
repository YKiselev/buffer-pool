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

import java.nio.Buffer;

import static java.util.Objects.requireNonNull;

/**
 * Buffer pool which is a collection of buffer pools. Allows to keep buffers of different sizes in different pools (of different pool size, possible).
 * to make them of  each of which is meant for specific buffer size range (like "small", "medium", "large", etc).
 *
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public final class RangedBufferPool<T extends Buffer> extends BufferPool<T> {

    /**
     * Buffer size range
     */
    public static final class Range<T extends Buffer> {

        private final int maxBufferSize;

        private final BufferPool<T> pool;

        public Range(int maxBufferSize, BufferPool<T> pool) {
            this.maxBufferSize = maxBufferSize;
            this.pool = requireNonNull(pool);
        }
    }

    private final Range<T>[] ranges;

    /**
     * Note: Supplied array of ranges must be sorted from smallest {@code Range@maxBufferSize} to largest!
     *
     * @param ranges the ranges to use.
     */
    @SafeVarargs
    public RangedBufferPool(Range<T>... ranges) {
        this.ranges = ranges.clone();
    }

    @Override
    public T acquire(int size) {
        for (Range<T> range : ranges) {
            if (size > range.maxBufferSize) {
                continue;
            }
            return range.pool.acquire(size);
        }
        return null;
    }

    @Override
    public void release(T buffer) {
        for (Range<T> range : ranges) {
            if (buffer.capacity() > range.maxBufferSize) {
                continue;
            }
            range.pool.release(buffer);
            return;
        }
        throw new IllegalStateException("Looks like supplied buffer does not fit in any range!");
    }

}

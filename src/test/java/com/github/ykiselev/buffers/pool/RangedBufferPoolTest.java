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

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertTrue;

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public class RangedBufferPoolTest {

    private final ByteBuffer small = ByteBuffer.allocate(100);

    private final ByteBuffer medium = ByteBuffer.allocate(1_000);

    private final ByteBuffer large = ByteBuffer.allocate(15_000);

    private final BufferPool<ByteBuffer> pool = new RangedBufferPool<>(
            new RangedBufferPool.Range<>(small.capacity(), new SimpleBufferPool<>(1, s -> small)),
            new RangedBufferPool.Range<>(medium.capacity(), new SimpleBufferPool<>(1, s -> medium)),
            new RangedBufferPool.Range<>(large.capacity(), new SimpleBufferPool<>(1, s -> large))
    );

    @Test
    public void shouldAcquireSmall() throws Exception {
        final ByteBuffer buffer = pool.acquire(100);
        assertTrue(buffer == small);
        pool.release(buffer);
    }

    @Test
    public void shouldAcquireMedium() throws Exception {
        final ByteBuffer buffer = pool.acquire(1_000);
        assertTrue(buffer == medium);
        pool.release(buffer);
    }

    @Test
    public void shouldAcquireLarge() throws Exception {
        final ByteBuffer buffer = pool.acquire(15_000);
        assertTrue(buffer == large);
        pool.release(buffer);
    }

}
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

import com.github.ykiselev.buffers.align.IntAdjustment;
import com.github.ykiselev.buffers.align.PowerOfTwoAdjustment;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public class SimpleBufferPoolTest {

    private final IntAdjustment adjustment = new PowerOfTwoAdjustment();

    private final BufferPool<ByteBuffer> pool = new SimpleBufferPool<>(
            2,
            s -> ByteBuffer.allocate(
                    adjustment.apply(s)
            )
    );

    @Test
    public void shouldNotFailForFirstTwo() throws Exception {
        final ByteBuffer b1 = pool.acquire(100, 0);
        try {
            assertEquals(128, b1.capacity());
            final ByteBuffer b2 = pool.acquire(300, 0);
            try {
                assertEquals(512, b2.capacity());
                assertNull(pool.acquire(10, 50));
            } finally {
                pool.release(b2);
            }
        } finally {
            pool.release(b1);
        }
    }

    @Test
    public void shouldRemoveSmallest() throws Exception {
        for (int i = 0, size = 2; i < 10; i++) {
            final ByteBuffer b1 = this.pool.acquire(size, 0);
            try {
                assertEquals(size, b1.capacity());
            } finally {
                this.pool.release(b1);
            }
            size *= 2;
        }
    }

    @Test
    public void shouldBeMultiThreadSafe() throws Exception {
        final List<Future<ByteBuffer>> futures;
        final ExecutorService service = Executors.newFixedThreadPool(4);
        try {
            final Supplier<Callable<ByteBuffer>> factory = () -> () -> {
                final ByteBuffer buffer;
                try {
                    buffer = pool.acquire(200, 200);
                    Thread.sleep(100);
                    pool.release(buffer);
                    return buffer;
                } catch (InterruptedException e) {
                    return null;
                }
            };
            futures = service.invokeAll(
                    Arrays.asList(
                            factory.get(),
                            factory.get(),
                            factory.get(),
                            factory.get()
                    ),
                    1,
                    TimeUnit.SECONDS
            );
        } finally {
            service.shutdown();
        }

        final Set<ByteBuffer> buffers = Collections.newSetFromMap(new IdentityHashMap<>());
        for (Future<ByteBuffer> future : futures) {
            final ByteBuffer buffer = future.get();
            assertNotNull(buffer);
            buffers.add(buffer);
        }
        assertEquals(2, buffers.size());

        service.awaitTermination(2, TimeUnit.SECONDS);
    }
}
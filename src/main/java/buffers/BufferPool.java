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

import com.google.common.base.Stopwatch;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Y.Kiselev on 04.06.2016.
 */
public final class BufferPool<T extends Buffer> {

    /**
     *
     */
    public interface AlignFunction {

        int apply(int value);
    }

    /**
     * @param <T>
     */
    public interface BufferFactory<T> {

        T create(int size);
    }

    private final List<T> pool = new ArrayList<>();

    private final int max;

    private final AlignFunction alignFunction;

    private final BufferFactory<T> factory;

    private volatile int counter;

    public BufferPool(int max, AlignFunction alignFunction, BufferFactory<T> factory) {
        this.max = max;
        this.alignFunction = alignFunction;
        this.factory = factory;
    }

    public T acquire(int size, int millis) throws TimeoutException {
        final int aligned = this.alignFunction.apply(size);
        final Stopwatch sw = Stopwatch.createStarted();
        do {
            final T result = tryAcquire(aligned);
            if (result != null) {
                return result;
            }
        } while (sw.elapsed(TimeUnit.MILLISECONDS) < millis);
        throw new TimeoutException();
    }

    private synchronized T tryAcquire(int aligned) {
        T result = findExisting(aligned);
        if (result == null) {
            if (this.counter == this.max) {
                removeSmallest();
            }
            if (this.counter < this.max) {
                result = this.factory.create(aligned);
                this.counter++;
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
        final Iterator<T> it = this.pool.iterator();
        while (it.hasNext()) {
            final T buffer = it.next();
            if (aligned <= buffer.capacity()) {
                it.remove();
                return buffer;
            }
        }
        return null;
    }

    public synchronized void release(T buffer) {
        this.pool.add(buffer);
    }

}

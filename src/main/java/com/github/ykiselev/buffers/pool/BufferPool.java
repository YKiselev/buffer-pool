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

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public abstract class BufferPool<T extends Buffer> {

    /**
     * @param size   the minimum required size of buffer in bytes
     * @return the buffer of requested size or {@code null} if new buffer cannot be allocated.
     */
    public abstract T acquire(int size);

    /**
     * @param buffer the buffer to return to pool
     */
    public abstract void release(T buffer);

}

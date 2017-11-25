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

/**
 * @author Yuriy Kiselev (uze@yandex.ru).
 */
public final class SizeAdjustingBufferFactory<T> implements BufferFactory<T> {

    private final BufferFactory<T> delegate;

    private final IntAdjustment adjustment;

    public SizeAdjustingBufferFactory(BufferFactory<T> delegate, IntAdjustment adjustment) {
        this.delegate = delegate;
        this.adjustment = adjustment;
    }

    @Override
    public T create(int size) {
        return delegate.create(
                adjustment.apply(size)
        );
    }
}

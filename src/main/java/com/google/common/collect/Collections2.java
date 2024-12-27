/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;

/**
 * Provides static methods for working with {@code Collection} instances.
 *
 * @author Chris Povirk
 * @author Mike Bostock
 * @author Jared Levy
 * @since 2.0 (imported from Google Collections Library)
 */
public final class Collections2 {
    private Collections2() {
    }

    /**
     * Returns a collection that applies {@code function} to each element of
     * {@code fromCollection}. The returned collection is a live view of {@code
     * fromCollection}; changes to one affect the other.
     *
     * <p>
     * The returned collection's {@code add()} and {@code addAll()} methods throw an
     * {@link UnsupportedOperationException}. All other collection methods are
     * supported, as long as {@code fromCollection} supports them.
     *
     * <p>
     * The returned collection isn't threadsafe or serializable, even if
     * {@code fromCollection} is.
     *
     * <p>
     * When a live view is <i>not</i> needed, it may be faster to copy the
     * transformed collection and use the copy.
     *
     * <p>
     * If the input {@code Collection} is known to be a {@code List}, consider
     * {@link Lists#transform}. If only an {@code Iterable} is available, use
     * {@link Iterables#transform}.
     */
    public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
        return new TransformedCollection<F, T>(fromCollection, function);
    }

    static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
            this.fromCollection = checkNotNull(fromCollection);
            this.function = checkNotNull(function);
        }

        @Override
        public void clear() {
            fromCollection.clear();
        }

        @Override
        public boolean isEmpty() {
            return fromCollection.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return Iterators.transform(fromCollection.iterator(), function);
        }

        @Override
        public int size() {
            return fromCollection.size();
        }
    }
}

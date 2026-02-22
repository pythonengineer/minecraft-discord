/*
 * Copyright (C) 2007 The Guava Authors
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

import static com.google.common.collect.CollectPreconditions.checkNonnegative;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import com.google.common.primitives.Ints;

/**
 * Static utility methods pertaining to {@link Map} instances (including
 * instances of {@link SortedMap}, {@link BiMap}, etc.). Also see this class's
 * counterparts {@link Lists}, {@link Sets} and {@link Queues}.
 *
 * <p>
 * See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/CollectionUtilitiesExplained#Maps">
 * {@code Maps}</a>.
 *
 * @author Kevin Bourrillion
 * @author Mike Bostock
 * @author Isaac Shum
 * @author Louis Wasserman
 * @since 2.0 (imported from Google Collections Library)
 */
public final class Maps {
    private Maps() {
    }

    /**
     * Returns a capacity that is sufficient to keep the map from being resized as
     * long as it grows no larger than expectedSize and the load factor is >= its
     * default (0.75).
     */
    static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            checkNonnegative(expectedSize, "expectedSize");
            return expectedSize + 1;
        }
        if (expectedSize < Ints.MAX_POWER_OF_TWO) {
            return expectedSize + expectedSize / 3;
        }
        return Integer.MAX_VALUE; // any large value
    }

    /**
     * Creates a <i>mutable</i>, empty {@code HashMap} instance.
     *
     * <p>
     * <b>Note:</b> if mutability is not required, use {@link ImmutableMap#of()}
     * instead.
     *
     * <p>
     * <b>Note:</b> if {@code K} is an {@code enum} type, use {@link #newEnumMap}
     * instead.
     *
     * @return a new, empty {@code HashMap}
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
}

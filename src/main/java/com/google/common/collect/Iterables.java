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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.CollectPreconditions.checkRemove;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * This class contains static utility methods that operate on or return objects
 * of type {@code Iterable}. Except as noted, each method has a corresponding
 * {@link Iterator}-based method in the {@link Iterators} class.
 *
 * <p>
 * <i>Performance notes:</i> Unless otherwise noted, all of the iterables
 * produced in this class are <i>lazy</i>, which means that their iterators only
 * advance the backing iteration when absolutely necessary.
 *
 * <p>
 * See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/CollectionUtilitiesExplained#Iterables">
 * {@code Iterables}</a>.
 *
 * @author Kevin Bourrillion
 * @author Jared Levy
 * @since 2.0 (imported from Google Collections Library)
 */
public final class Iterables {
	private Iterables() {
	}

	/**
	 * Copies an iterable's elements into an array.
	 *
	 * @param iterable the iterable to copy
	 * @param type     the type of the elements
	 * @return a newly-allocated array into which all the elements of the iterable
	 *         have been copied
	 */
	public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
		Collection<? extends T> collection = toCollection(iterable);
		T[] array = ObjectArrays.newArray(type, collection.size());
		return collection.toArray(array);
	}

	/**
	 * Copies an iterable's elements into an array.
	 *
	 * @param iterable the iterable to copy
	 * @return a newly-allocated array into which all the elements of the iterable
	 *         have been copied
	 */
	static Object[] toArray(Iterable<?> iterable) {
		return toCollection(iterable).toArray();
	}

	/**
	 * Converts an iterable into a collection. If the iterable is already a
	 * collection, it is returned. Otherwise, an {@link java.util.ArrayList} is
	 * created with the contents of the iterable in the same iteration order.
	 */
	private static <E> Collection<E> toCollection(Iterable<E> iterable) {
		return (iterable instanceof Collection) ? (Collection<E>) iterable : Lists.newArrayList(iterable.iterator());
	}
}

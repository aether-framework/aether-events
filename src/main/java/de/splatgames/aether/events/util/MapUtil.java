/*
 * Copyright (c) 2025 Splatgames.de Software and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.splatgames.aether.events.util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class providing convenience methods for working with {@link Map} instances.
 * This class offers a method to create immutable maps from an array of {@link Pair} objects.
 *
 * <p>The maps created using this utility method are <i>unmodifiable</i>, meaning that
 * any attempt to modify them will result in an {@link UnsupportedOperationException}.
 * This is useful when working with predefined key-value pairs that should remain
 * constant throughout the application's lifetime.</p>
 *
 * <h2><a id="immutability">Immutability</a></h2>
 * <p>The maps returned by this utility method possess the following characteristics:</p>
 *
 * <ul>
 *     <li>They are <i>unmodifiable</i>: Any attempt to add, remove, or update a key-value
 *         mapping will result in an {@code UnsupportedOperationException}.</li>
 *     <li>They do not permit {@code null} keys or values. If a {@link Pair} contains a
 *         {@code null} key or value, a {@link NullPointerException} will be thrown at creation time.</li>
 *     <li>They preserve the last occurrence of duplicate keys. If multiple {@link Pair} objects
 *         contain the same key, the last one in the provided array will be used.</li>
 *     <li>The iteration order of the returned map follows the order in which the pairs
 *         are provided in the input array.</li>
 *     <li>They are not serializable. If serialization is required, consider using a
 *         {@link java.io.Serializable} implementation like {@link java.util.LinkedHashMap}.</li>
 * </ul>
 *
 * <h2><a id="null-safety">Null-Safety</a></h2>
 * <p>To maintain consistency and prevent runtime errors, this utility method strictly
 * enforces null safety:</p>
 *
 * <ul>
 *     <li>If the provided {@code pairs} array itself is {@code null}, a
 *         {@link NullPointerException} is thrown.</li>
 *     <li>If any element within the {@code pairs} array is {@code null}, a
 *         {@code NullPointerException} is thrown.</li>
 *     <li>If any key or value inside a {@link Pair} is {@code null}, a
 *         {@code NullPointerException} is thrown.</li>
 * </ul>
 *
 * <h2><a id="usage">Usage Example</a></h2>
 * <p>The {@code of} method allows for concise and immutable map creation:</p>
 *
 * <pre>{@code
 * Map<String, Integer> scores = MapUtil.of(
 *     new Pair<>("Alice", 90),
 *     new Pair<>("Bob", 85),
 *     new Pair<>("Charlie", 92)
 * );
 * System.out.println(scores); // {Alice=90, Bob=85, Charlie=92}
 * }</pre>
 *
 * <p>Attempting to modify the returned map results in an exception:</p>
 *
 * <pre>{@code
 * scores.put("David", 88); // Throws UnsupportedOperationException
 * }</pre>
 *
 * <h2><a id="performance">Performance Considerations</a></h2>
 * <p>This method constructs a new {@link HashMap}, inserts the key-value pairs,
 * and then wraps it using {@link Map#copyOf(Map)}. The time complexity is O(n),
 * where n is the number of pairs. Because the result is immutable, it may be
 * more memory-efficient than using a modifiable {@code HashMap} if frequent
 * updates are not required.</p>
 *
 * <h2><a id="limitations">Limitations</a></h2>
 * <p>While useful for small-scale immutable maps, this method may not be ideal for
 * large datasets due to the overhead of creating a new {@code HashMap} before
 * copying it. For larger maps, consider using a builder pattern or an alternative
 * map implementation that suits performance needs.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public final class MapUtil {

    /**
     * Creates an immutable {@code Map} from an array of {@link Pair} objects.
     * Each {@link Pair} represents a key-value mapping that will be included
     * in the returned map.
     *
     * <p>If multiple pairs contain the same key, the last occurrence will
     * overwrite the previous value.</p>
     *
     * <p>The resulting map is immutable, meaning that any attempt to modify
     * it will result in an {@link UnsupportedOperationException}.</p>
     *
     * @param <K>   the type of keys in the returned map
     * @param <V>   the type of values in the returned map
     * @param pairs an array of {@link Pair} objects containing key-value mappings;
     *              must not be {@code null}, and none of the pairs may be {@code null}
     * @return an immutable {@code Map} containing the provided key-value pairs
     * @throws NullPointerException if {@code pairs} is {@code null} or contains a {@code null} pair,
     *                              or if any pair has a {@code null} key or value
     */
    @SafeVarargs
    public static <K, V> Map<K, V> of(@NotNull final Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs) {
            if (pair.key() == null) {
                throw new NullPointerException("Key cannot be null");
            }
            if (pair.value() == null) {
                throw new NullPointerException("Value cannot be null");
            }
            map.put(pair.key(), pair.value());
        }

        return Map.copyOf(map);
    }

    /**
     * Private constructor to prevent instantiation.
     *
     * @throws AssertionError if an attempt is made to instantiate this class
     */
    private MapUtil() {
        throw new AssertionError("Cannot instantiate utility class");
    }
}

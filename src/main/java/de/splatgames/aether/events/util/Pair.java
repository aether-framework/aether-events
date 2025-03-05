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

/**
 * A simple immutable container that holds a key-value pair.
 * This class is useful for returning two related values from a method or
 * for use in map-like structures that do not require a full {@link java.util.Map}.
 *
 * <p>{@code Pair} instances are immutable and support generic types for both
 * keys and values. The key and value can be of any type, including {@code null}.
 * However, users should be cautious when using mutable objects as keys,
 * as they may lead to unexpected behavior when used in collections
 * that rely on key equality.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * Pair<String, Integer> pair = Pair.of("Alice", 30);
 * System.out.println(pair.key() + " is " + pair.value() + " years old.");
 * }</pre>
 *
 * <h2>Equality and Hashing</h2>
 * <p>The {@code Pair} class follows standard equality and hash code behavior:</p>
 * <ul>
 *     <li>Two {@code Pair} instances are considered equal if both their key and value
 *         are equal according to {@link Object#equals(Object)}.</li>
 *     <li>The hash code is computed based on the hash codes of the key and value.</li>
 *     <li>The {@code toString()} method returns a string representation in the form
 *         {@code (key, value)}.</li>
 * </ul>
 *
 * <h2>Limitations</h2>
 * <p>Unlike entries in {@link java.util.Map}, a {@code Pair} does not enforce
 * uniqueness of keys when used in collections. If unique keys are required,
 * consider using a {@code Map<K, V>} instead.</p>
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public record Pair<K, V>(K key, V value) {

    /**
     * Creates a new {@code Pair} with the given key and value.
     *
     * <p>This method provides a convenient way to create pairs without explicitly
     * calling the constructor.</p>
     *
     * @param <K>   the type of the key
     * @param <V>   the type of the value
     * @param key   the key, can be {@code null}
     * @param value the value, can be {@code null}
     * @return a new {@code Pair} containing the specified key and value
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
}

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

/**
 * Provides utility classes for the Aether Event System.
 * <p>
 * This package contains various helper classes that facilitate event timing,
 * immutable map creation, and generic key-value storage. These utilities are
 * designed to improve performance, code clarity, and ease of use when working
 * with the Aether event framework.
 * </p>
 *
 * <h2>Key Components</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.util.EventTiming EventTiming} – Measures execution
 *     time of event-related operations with nanosecond precision.</li>
 *     <li>{@link de.splatgames.aether.events.util.MapUtil MapUtil} – Provides utility methods
 *     for creating immutable maps from key-value pairs.</li>
 *     <li>{@link de.splatgames.aether.events.util.Pair Pair} – A generic immutable container
 *     for storing two related values.</li>
 * </ul>
 *
 * <h2>Usage Examples</h2>
 * <p><b>Timing event execution with {@link de.splatgames.aether.events.util.EventTiming EventTiming}:</b></p>
 * <pre>{@code
 * EventTiming timing = new EventTiming("EventProcessing");
 *
 * // Execute event handling logic
 * timing.split();
 *
 * // Execute additional logic
 * timing.stop();
 *
 * System.out.println("Total duration: " + timing.getDurationMillis() + " ms");
 * }</pre>
 *
 * <p><b>Creating an immutable map with {@link de.splatgames.aether.events.util.MapUtil MapUtil}:</b></p>
 * <pre>{@code
 * Map<String, Integer> scores = MapUtil.of(
 *     Pair.of("Alice", 90),
 *     Pair.of("Bob", 85),
 *     Pair.of("Charlie", 92)
 * );
 * }</pre>
 *
 * <p><b>Using {@link de.splatgames.aether.events.util.Pair Pair} for key-value storage:</b></p>
 * <pre>{@code
 * Pair<String, Integer> playerScore = Pair.of("Player1", 1500);
 * System.out.println(playerScore.key() + " has a score of " + playerScore.value());
 * }</pre>
 *
 * <h2>Performance Considerations</h2>
 * <ul>
 *     <li>{@code EventTiming} operates with nanosecond precision and should be used
 *     for fine-grained performance analysis.</li>
 *     <li>{@code MapUtil.of()} is optimized for small, immutable maps and is not
 *     intended for large-scale dynamic mappings.</li>
 *     <li>{@code Pair} is immutable and provides safer key-value storage but does
 *     not enforce unique keys.</li>
 * </ul>
 *
 * <h2>Best Practices</h2>
 * <ul>
 *     <li>Use {@code EventTiming} only for performance-critical operations to avoid
 *     unnecessary overhead.</li>
 *     <li>Prefer {@code MapUtil.of()} when working with static mappings to ensure
 *     immutability and thread safety.</li>
 *     <li>Use {@code Pair} for temporary key-value storage, but prefer {@code Map}
 *     for structured and scalable key-value associations.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @see de.splatgames.aether.events.util.EventTiming EventTiming
 * @see de.splatgames.aether.events.util.MapUtil MapUtil
 * @see de.splatgames.aether.events.util.Pair Pair
 * @since aether-events 1.0.0
 */
package de.splatgames.aether.events.util;

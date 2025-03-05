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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for measuring execution time of events and tracking intermediate splits.
 * This class provides nanosecond-precision timing and allows for multiple time splits
 * to be recorded before stopping the timer.
 *
 * <p>The timer starts upon instantiation and can be stopped using {@link #stop()}.
 * Intermediate timestamps (splits) can be recorded using {@link #split()}, allowing
 * for performance analysis of different execution segments.</p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * EventTiming timing = new EventTiming("ExampleTask");
 *
 * // Execute some logic
 * timing.split();
 *
 * // Execute more logic
 * timing.stop();
 *
 * System.out.println("Total time: " + timing.getDurationMillis() + " ms");
 * System.out.println("Splits: " + timing.getSplitsMillis());
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>Instances of this class are not thread-safe and should be used within a single thread.
 * If shared among multiple threads, external synchronization is required.</p>
 *
 * <h2>Behavior</h2>
 * <ul>
 *     <li>Once stopped, the timing cannot be resumed.</li>
 *     <li>Calling {@link #split()} after stopping will result in an {@link IllegalStateException}.</li>
 *     <li>Durations are measured in nanoseconds but exposed in milliseconds for easier interpretation.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public class EventTiming {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventTiming.class);

    private final String name;
    private final long startTime;
    private long endTime;
    private final List<Long> splits;

    private boolean stopped;

    /**
     * Constructs a new {@code EventTiming} instance with the specified name.
     * The timer starts immediately upon creation.
     *
     * @param name the name of the event being timed; must not be {@code null}
     * @throws NullPointerException if {@code name} is {@code null}
     */
    public EventTiming(@NotNull String name) {
        this.name = name;
        this.startTime = System.nanoTime();
        this.splits = new ArrayList<>();
        this.stopped = false;
    }

    /**
     * Records a split time from the last recorded timestamp.
     * This allows for tracking intermediate execution segments.
     *
     * @throws IllegalStateException if the timing has already been stopped
     */
    public void split() {
        if (stopped) {
            throw new IllegalStateException("Cannot split timing after it has been stopped.");
        }
        this.splits.add(System.nanoTime());
    }

    /**
     * Stops the timing, preventing further splits from being recorded.
     * This method can be called multiple times, but only the first invocation
     * will be effective.
     */
    public void stop() {
        if (!stopped) {
            this.endTime = System.nanoTime();
            this.stopped = true;
        }
    }

    /**
     * Returns the total duration in nanoseconds from the start to the stop time.
     *
     * @return the total execution time in nanoseconds
     * @throws IllegalStateException if the timing has not been stopped
     */
    public long getDurationNano() {
        if (!stopped) {
            throw new IllegalStateException("Timing has not been stopped yet.");
        }
        return endTime - startTime;
    }

    /**
     * Returns the total duration in nanoseconds from the start to the stop time.
     *
     * @return the total execution time in nanoseconds
     * @throws IllegalStateException if the timing has not been stopped
     */
    public double getDurationMillis() {
        return getDurationNano() / 1_000_000.0;
    }

    /**
     * Returns a list of recorded split durations in milliseconds.
     * The first value represents the time from start to the first split,
     * the second value represents the time from the first to the second split, and so on.
     * If the timer has been stopped, the last value represents the time from the last split to the stop time.
     *
     * @return a list of time intervals in milliseconds
     */
    public List<Double> getSplitsMillis() {
        List<Double> splitDurations = new ArrayList<>();
        long previousTime = startTime;

        for (long splitTime : splits) {
            splitDurations.add((splitTime - previousTime) / 1_000_000.0);
            previousTime = splitTime;
        }

        if (stopped) {
            splitDurations.add((endTime - previousTime) / 1_000_000.0);
        }

        return splitDurations;
    }

    /**
     * Returns the average split duration in milliseconds.
     * If no splits are recorded, the total duration is returned instead.
     *
     * @return the average time per split in milliseconds
     */
    public double getAverageSplitMillis() {
        List<Double> splitDurations = getSplitsMillis();
        if (splitDurations.isEmpty()) {
            return getDurationMillis();
        }
        return splitDurations.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    /**
     * Logs the total execution time and all split times using SLF4J.
     * The timer is automatically stopped if it has not been stopped yet.
     *
     * <p>The log output format is:</p>
     * <pre>{@code
     * Timing [ExampleTask]: 15.2 ms (Splits: [5.0, 10.2])
     * }</pre>
     */
    public void logTiming() {
        stop();
        LOGGER.info("Timing [{}]: {} ms (Splits: {})", name, getDurationMillis(), getSplitsMillis());
    }

    /**
     * Returns a string representation of the timing result.
     * The format is:
     * <pre>{@code
     * Timing [TaskName]: 12.345 ms (Splits: [4.321, 8.024])
     * }</pre>
     *
     * @return a formatted string representation of the timing results
     */
    @Override
    public String toString() {
        return String.format("Timing [%s]: %.3f ms (Splits: %s)", name, getDurationMillis(), getSplitsMillis());
    }
}

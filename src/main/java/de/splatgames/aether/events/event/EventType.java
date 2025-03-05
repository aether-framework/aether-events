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

package de.splatgames.aether.events.event;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents different types of events that can be categorized based on their severity or purpose.
 *
 * <p>Each {@code EventType} has an associated name and a corresponding {@link EventPriority}
 * that determines its execution order relative to other events.</p>
 *
 * <h2>Event Types</h2>
 * <ul>
 *     <li>{@link #INFO} - General informational events (Low priority).</li>
 *     <li>{@link #WARNING} - Events indicating potential issues (Normal priority).</li>
 *     <li>{@link #ERROR} - Events representing errors (High priority).</li>
 *     <li>{@link #DEBUG} - Debugging events, usually not affecting execution (Monitor priority).</li>
 *     <li>{@link #EXCEPTION} - Events triggered by exceptions (Highest priority).</li>
 *     <li>{@link #UNKNOWN} - A fallback for unclassified events (Lowest priority).</li>
 * </ul>
 *
 * <p>The assigned priority helps determine the order in which events should be processed.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public enum EventType {

    /**
     * Represents an informational event.
     * <p>Typically used for general logs or status updates.</p>
     */
    INFO("Info", EventPriority.LOW),

    /**
     * Represents a warning event.
     * <p>Used to indicate potential problems that may require attention.</p>
     */
    WARNING("Warning", EventPriority.NORMAL),

    /**
     * Represents an error event.
     * <p>Indicates that an error has occurred and may require handling.</p>
     */
    ERROR("Error", EventPriority.HIGH),

    /**
     * Represents a debug event.
     * <p>Usually used for internal debugging purposes without affecting execution.</p>
     */
    DEBUG("Debug", EventPriority.MONITOR),

    /**
     * Represents an exception event.
     * <p>Used when an exception occurs during event execution.</p>
     */
    EXCEPTION("Exception", EventPriority.HIGHEST),

    /**
     * Represents an unknown or unclassified event.
     * <p>Acts as a fallback category for events that do not fit into other types.</p>
     */
    UNKNOWN("Unknown", EventPriority.LOWEST);

    private final String name;
    private final EventPriority priority;

    /**
     * Constructs a new {@code EventType} with the specified name and priority.
     *
     * @param name     the human-readable name of the event type
     * @param priority the priority level associated with this event type
     * @throws NullPointerException if {@code name} or {@code priority} is {@code null}
     */
    EventType(@NotNull final String name, @NotNull final EventPriority priority) {
        this.name = name;
        this.priority = priority;
    }

    /**
     * Returns the event type associated with the given name.
     *
     * <p>The name comparison is case-insensitive.</p>
     *
     * @param name the name of the event type to look up
     * @return the corresponding {@code EventType}, or {@code null} if no match is found
     * @throws NullPointerException if {@code name} is {@code null}
     */
    @Nullable
    public static EventType getEventType(@NotNull final String name) {
        for (final EventType eventType : values()) {
            if (eventType.getName().equalsIgnoreCase(name)) {
                return eventType;
            }
        }
        return null;
    }

    /**
     * Returns the event type associated with the given priority.
     *
     * @param priority the priority to look up
     * @return the corresponding {@code EventType}, or {@code null} if no match is found
     * @throws NullPointerException if {@code priority} is {@code null}
     */
    @Nullable
    public static EventType getEventType(@NotNull final EventPriority priority) {
        for (final EventType eventType : values()) {
            if (eventType.getPriority().equals(priority)) {
                return eventType;
            }
        }
        return null;
    }

    /**
     * Returns the name of this event type.
     *
     * @return the name of the event type
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns the priority associated with this event type.
     *
     * @return the priority of the event type
     */
    @NotNull
    public EventPriority getPriority() {
        return priority;
    }

    /**
     * Returns the string representation of this event type, which is its name.
     *
     * @return the name of the event type
     */
    @Override
    public String toString() {
        return name;
    }
}

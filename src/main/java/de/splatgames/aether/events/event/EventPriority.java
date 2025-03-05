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
 * Defines the priority levels for event execution.
 *
 * <p>Each event handler is assigned a priority, determining the order in which it is executed
 * relative to other handlers for the same event type. Handlers with a higher priority
 * are executed before those with a lower priority.</p>
 *
 * <h2>Priority Levels</h2>
 * <p>The available priorities, in order of execution:</p>
 * <ul>
 *     <li>{@link #LOWEST} - Executed first, used for preliminary modifications.</li>
 *     <li>{@link #LOW} - Executed after {@code LOWEST}, typically for secondary modifications.</li>
 *     <li>{@link #NORMAL} - Default priority, used for general event handling.</li>
 *     <li>{@link #HIGH} - Executed before {@code NORMAL}, often for override behavior.</li>
 *     <li>{@link #HIGHEST} - Executed last before {@code MONITOR}, for critical handling.</li>
 *     <li>{@link #MONITOR} - Executed last, intended for observing final event state without modification.</li>
 * </ul>
 *
 * <p>Handlers registered with {@code MONITOR} should avoid modifying the event, as
 * they are meant to act as observers.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public enum EventPriority {

    /**
     * The lowest priority, executed first.
     * <p>Used for early modifications before other handlers process the event.</p>
     */
    LOWEST(0),

    /**
     * A low priority, executed after {@link #LOWEST}.
     * <p>Typically used for non-critical event modifications.</p>
     */
    LOW(1),

    /**
     * The default priority for event handlers.
     * <p>Executed after {@link #LOW} and before {@link #HIGH}.</p>
     */
    NORMAL(2),

    /**
     * A high priority, executed before {@link #NORMAL}.
     * <p>Used when an event needs to be modified before lower-priority handlers process it.</p>
     */
    HIGH(3),

    /**
     * The highest priority before {@link #MONITOR}.
     * <p>Often used for final modifications before an event is considered fully handled.</p>
     */
    HIGHEST(4),

    /**
     * The last priority level, used for monitoring events without modifying them.
     * <p>Handlers at this level should only observe and log events.</p>
     */
    MONITOR(5);

    private final int slot;

    /**
     * Constructs a new {@code EventPriority} with the given priority slot.
     *
     * @param slot the execution order value of the priority
     */
    EventPriority(final int slot) {
        this.slot = slot;
    }

    /**
     * Returns the numerical priority slot for this event priority.
     *
     * @return the priority slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Returns the {@code EventPriority} associated with the given numerical slot.
     *
     * @param slot the priority slot to look up
     * @return the corresponding {@code EventPriority}, or {@code null} if no match is found
     */
    @Nullable
    public static EventPriority getPriority(final int slot) {
        for (final EventPriority priority : values()) {
            if (priority.getSlot() == slot) {
                return priority;
            }
        }
        return null;
    }

    /**
     * Returns the {@code EventPriority} associated with the given name.
     *
     * <p>The name comparison is case-insensitive.</p>
     *
     * @param name the name of the priority to look up
     * @return the corresponding {@code EventPriority}, or {@code null} if no match is found
     * @throws NullPointerException if {@code name} is {@code null}
     */
    @Nullable
    public static EventPriority getPriority(@NotNull final String name) {
        for (final EventPriority priority : values()) {
            if (priority.name().equalsIgnoreCase(name)) {
                return priority;
            }
        }
        return null;
    }
}

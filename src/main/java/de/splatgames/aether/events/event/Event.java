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

/**
 * Represents a base class for all events in the event system.
 *
 * <p>All events must extend this class to be processed by the {@code EventBus}.</p>
 *
 * <h2>Event Name</h2>
 * <p>Each event has a name, which defaults to the simple class name if not explicitly set.
 * The name is primarily used for debugging and logging purposes.</p>
 *
 * <h2>Synchronous vs. Asynchronous Events</h2>
 * <p>Events can be marked as asynchronous during instantiation. An asynchronous event
 * is not guaranteed to be executed on the main thread, and event handlers should be
 * designed accordingly.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public abstract class Event {
    /**
     * The name of the event, lazily initialized based on the class name if not set.
     */
    protected String name;

    /**
     * Indicates whether this event is asynchronous.
     */
    private final boolean async;

    /**
     * Constructs a new event, defaulting to synchronous execution.
     */
    protected Event() {
        this(false);
    }

    /**
     * Constructs a new event with the specified asynchronous flag.
     *
     * @param isAsync {@code true} if the event is asynchronous, {@code false} otherwise
     */
    protected Event(final boolean isAsync) {
        this.async = isAsync;
    }

    /**
     * Returns the name of the event.
     *
     * <p>If the name has not been explicitly set, it defaults to the simple class name.</p>
     *
     * @return the name of the event
     */
    public String getEventName() {
        if (this.name == null) {
            this.name = getClass().getSimpleName();
        }
        return this.name;
    }

    /**
     * Returns whether this event is marked as asynchronous.
     *
     * <p>Asynchronous events may be processed outside the main thread,
     * and event listeners should ensure thread safety when handling them.</p>
     *
     * @return {@code true} if the event is asynchronous, {@code false} otherwise
     */
    public final boolean isAsync() {
        return this.async;
    }
}

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
 * Represents an event that can be cancelled.
 *
 * <p>Events implementing this interface provide a mechanism to prevent their execution
 * by marking them as cancelled. Event handlers can check the cancellation state
 * using {@link #isCancelled()} and modify it using {@link #setCancelled(boolean)}.</p>
 *
 * <h2>Event Handling Behavior</h2>
 * <ul>
 *     <li>If an event is cancelled, event listeners may choose to ignore it based on their configuration.</li>
 *     <li>Methods annotated with {@link de.splatgames.aether.events.annotation.Subscribe} can check
 *         {@code event.isCancelled()} to determine if further processing is necessary.</li>
 *     <li>If a listener has {@code ignoreCancelled = true}, it will not receive cancelled events.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public interface Cancellable {

    /**
     * Returns whether this event has been cancelled.
     *
     * @return {@code true} if the event is cancelled, {@code false} otherwise
     */
    boolean isCancelled();

    /**
     * Sets the cancellation state of this event.
     *
     * <p>If set to {@code true}, event handlers may choose to ignore further processing,
     * depending on their configuration.</p>
     *
     * @param cancelled {@code true} to cancel the event, {@code false} to allow execution
     */
    void setCancelled(final boolean cancelled);
}

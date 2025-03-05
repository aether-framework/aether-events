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
 * Provides the core event model for the Aether Event System.
 * <p>
 * This package contains the fundamental building blocks required to define,
 * categorize, and execute events. It includes the base {@link de.splatgames.aether.events.event.Event Event}
 * class, event priority definitions, and mechanisms for handling cancellable
 * and executable events.
 * </p>
 *
 * <h2>Key Components</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.event.Event Event} – The base class for all events.</li>
 *     <li>{@link de.splatgames.aether.events.event.Cancellable Cancellable} – An interface for events that
 *     can be cancelled, preventing further processing.</li>
 *     <li>{@link de.splatgames.aether.events.event.EventExecutor EventExecutor} – Defines how event listeners
 *     execute their registered methods.</li>
 *     <li>{@link de.splatgames.aether.events.event.EventPriority EventPriority} – Specifies the execution order
 *     of event listeners.</li>
 *     <li>{@link de.splatgames.aether.events.event.EventType EventType} – Categorizes events based on their
 *     severity and purpose.</li>
 * </ul>
 *
 * <h2>Event Lifecycle</h2>
 * <p>All events extend {@link de.splatgames.aether.events.event.Event Event} and follow a standard lifecycle:</p>
 * <ol>
 *     <li>An event is instantiated and optionally marked as asynchronous.</li>
 *     <li>The event is passed to the {@code EventBus} for dispatching.</li>
 *     <li>Listeners process the event based on priority and cancellation state.</li>
 * </ol>
 * <pre>{@code
 * public class ObjectCreationEvent extends Event implements Cancellable {
 *     private boolean cancelled;
 *
 *     public ObjectCreationEvent() {
 *         super(false); // Synchronous event
 *     }
 *
 *     @Override
 *     public boolean isCancelled() {
 *         return cancelled;
 *     }
 *
 *     @Override
 *     public void setCancelled(boolean cancelled) {
 *         this.cancelled = cancelled;
 *     }
 * }
 * }</pre>
 *
 * <h2>Event Prioritization</h2>
 * <p>Listeners are executed in order of their {@link de.splatgames.aether.events.event.EventPriority EventPriority}:</p>
 * <ul>
 *     <li>{@code LOWEST} – Executed first, used for preliminary modifications.</li>
 *     <li>{@code LOW} – Secondary modifications.</li>
 *     <li>{@code NORMAL} – Default priority, general handling.</li>
 *     <li>{@code HIGH} – Override behavior.</li>
 *     <li>{@code HIGHEST} – Final modifications.</li>
 *     <li>{@code MONITOR} – Observers, should not modify the event.</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 * <p>Events may be processed synchronously or asynchronously, as determined by
 * the {@link de.splatgames.aether.events.event.Event#isAsync() Event#isAsync()} flag. Developers should ensure that asynchronous events
 * do not cause concurrency issues.</p>
 *
 * <h2>Implementation Notes</h2>
 * <ul>
 *     <li>All event classes should be immutable where possible to ensure thread safety.</li>
 *     <li>Event names default to the class name unless explicitly set.</li>
 *     <li>Cancellable events should properly respect the {@code isCancelled()} state.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @see de.splatgames.aether.events.event.Event Event
 * @see de.splatgames.aether.events.event.Cancellable Cancellable
 * @see de.splatgames.aether.events.event.EventPriority EventPriority
 * @see de.splatgames.aether.events.event.EventType EventType
 * @since aether-events 1.0.0
 */
package de.splatgames.aether.events.event;

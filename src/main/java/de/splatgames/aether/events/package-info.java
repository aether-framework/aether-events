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
 * Provides the core components of the Aether Event System.
 * <p>
 * This package contains the fundamental interfaces and classes required to
 * implement a flexible, high-performance event-driven architecture. It serves
 * as the entry point for event handling and facilitates event registration,
 * dispatching, and execution.
 * </p>
 *
 * <h2>Key Components</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.bus.EventBus EventBus} – The central event dispatcher
 *     that manages event subscriptions and execution.</li>
 *     <li>{@link de.splatgames.aether.events.listener.Listener Listener} – A marker interface for
 *     event-handling classes.</li>
 *     <li>{@link de.splatgames.aether.events.event.Event Event} – The base class for all
 *     event types.</li>
 *     <li>{@link de.splatgames.aether.events.event.Cancellable Cancellable} – An interface for
 *     events that can be cancelled before further processing.</li>
 *     <li>{@link de.splatgames.aether.events.event.EventPriority EventPriority} – Defines the priority
 *     levels for event execution.</li>
 *     <li>{@link de.splatgames.aether.events.event.EventType EventType} – Categorizes different
 *     types of events.</li>
 * </ul>
 *
 * <h2>Event Flow</h2>
 * <p>The Aether Event System follows a structured event processing model:</p>
 * <ol>
 *     <li>An event class extends {@link de.splatgames.aether.events.event.Event Event}.</li>
 *     <li>A listener class implements {@link de.splatgames.aether.events.listener.Listener Listener} and subscribes to the event
 *     using {@link de.splatgames.aether.events.annotation.Subscribe @Subscribe}.</li>
 *     <li>The listener is registered to an {@link de.splatgames.aether.events.bus.EventBus EventBus} instance.</li>
 *     <li>When the event occurs, it is dispatched by the event bus and executed
 *     in order of priority.</li>
 * </ol>
 *
 * <h2>Basic Usage</h2>
 * <p>Registering and dispatching an event:</p>
 * <pre>{@code
 * public class CustomEvent extends Event {
 *     private final String message;
 *
 *     public CustomEvent(String message) {
 *         this.message = message;
 *     }
 *
 *     public String getMessage() {
 *         return message;
 *     }
 * }
 *
 * public class MyEventListener implements Listener {
 *     @Subscribe(priority = EventPriority.NORMAL)
 *     public void onCustomEvent(CustomEvent event) {
 *         System.out.println("Received event: " + event.getMessage());
 *     }
 * }
 *
 * EventBus eventBus = EventBus.builder().build();
 * eventBus.subscribe(new MyEventListener());
 * eventBus.fire(new CustomEvent("Hello, World!"));
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>Event handling can be synchronous or asynchronous. By default, events are
 * processed in the main thread. Asynchronous execution is supported, but
 * developers must ensure thread safety when handling events concurrently.</p>
 *
 * <h2>Performance Considerations</h2>
 * <ul>
 *     <li>Event listeners should be optimized to minimize execution time.</li>
 *     <li>Heavy computations should be handled asynchronously to avoid blocking
 *     the event loop.</li>
 *     <li>Using appropriate {@link de.splatgames.aether.events.event.EventPriority EventPriority} levels helps optimize event flow.</li>
 * </ul>
 *
 * <h2>Best Practices</h2>
 * <ul>
 *     <li>Ensure event handlers catch exceptions to prevent event flow disruption.</li>
 *     <li>Use {@code @Subscribe} annotations to define clear event-handling logic.</li>
 *     <li>Utilize the {@code EventBus} builder for configuration flexibility.</li>
 *     <li>Avoid modifying events in {@code MONITOR}-level listeners, as they are
 *     meant for observation.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @see de.splatgames.aether.events.bus.EventBus EventBus
 * @see de.splatgames.aether.events.listener.Listener Listener
 * @see de.splatgames.aether.events.event.Event Event
 * @see de.splatgames.aether.events.event.Cancellable Cancellable
 * @see de.splatgames.aether.events.event.EventPriority EventPriority
 * @see de.splatgames.aether.events.event.EventType EventType
 * @since aether-events 1.0.0
 */
package de.splatgames.aether.events;

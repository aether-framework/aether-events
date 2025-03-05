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
 * Provides the listener system for the Aether Event System.
 * <p>
 * This package contains the fundamental components responsible for event listener
 * registration and execution. Event listeners are the core mechanism through which
 * external components can react to events dispatched by the event bus.
 * </p>
 *
 * <h2>Key Components</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.listener.Listener Listener} – A marker interface that all
 *     event listeners must implement to receive events.</li>
 *     <li>{@link de.splatgames.aether.events.listener.RegisteredListener RegisteredListener} – Represents a listener
 *     that has been registered to an {@code EventBus}, including its execution logic.</li>
 * </ul>
 *
 * <h2>Event Listener Registration</h2>
 * <p>To receive events, a class must implement the {@link de.splatgames.aether.events.listener.Listener Listener} interface and define
 * one or more methods annotated with {@link de.splatgames.aether.events.annotation.Subscribe @Subscribe}.
 * These methods will automatically be invoked when the corresponding event is dispatched.</p>
 *
 * <h2>Basic Usage</h2>
 * <p>Example of an event listener that listens for a custom event:</p>
 * <pre>{@code
 * public class MyEventListener implements Listener {
 *
 *     @Subscribe
 *     public void onCustomEvent(CustomEvent event) {
 *         System.out.println("Received event: " + event);
 *     }
 * }
 * }</pre>
 *
 * <p>To activate the listener, it must be registered in the event bus:</p>
 * <pre>{@code
 * EventBus eventBus = EventBus.builder().build();
 * eventBus.subscribe(new MyEventListener());
 * }</pre>
 *
 * <h2>Listener Execution and Priority</h2>
 * <p>Registered listeners are managed through {@link de.splatgames.aether.events.listener.RegisteredListener RegisteredListener}, which stores
 * additional metadata, such as:</p>
 * <ul>
 *     <li><b>Execution Priority:</b> Determines the order in which listeners are invoked
 *     (see {@link de.splatgames.aether.events.event.EventPriority EventPriority}).</li>
 *     <li><b>Cancellation Behavior:</b> Listeners can ignore cancelled events based on
 *     their configuration.</li>
 *     <li><b>Threading:</b> Asynchronous events are executed in a separate thread when applicable.</li>
 * </ul>
 *
 * <h2>Performance Considerations</h2>
 * <p>Listeners should be designed efficiently to prevent performance bottlenecks. Heavy operations
 * should be performed asynchronously where possible. Additionally, excessive logging in event handlers
 * may impact performance.</p>
 *
 * <h2>Best Practices</h2>
 * <ul>
 *     <li>Use appropriate event priorities to ensure correct execution order.</li>
 *     <li>Avoid modifying event states in {@code MONITOR}-level listeners.</li>
 *     <li>Ensure thread safety when handling asynchronous events.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @see de.splatgames.aether.events.listener.Listener Listener
 * @see de.splatgames.aether.events.listener.RegisteredListener RegisteredListener
 * @see de.splatgames.aether.events.event.EventPriority EventPriority
 * @see de.splatgames.aether.events.bus.EventBus EventBus
 * @since aether-events 1.0.0
 */
package de.splatgames.aether.events.listener;

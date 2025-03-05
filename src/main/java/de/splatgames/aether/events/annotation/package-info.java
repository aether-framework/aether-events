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
 * Provides annotations for event-driven programming within Aether Events.
 * <p>
 * This package contains key annotations used for defining and handling event-driven
 * functionality in the {@code Aether Events} module. These annotations allow developers
 * to register event listeners and control warning behaviors related to method usage.
 * </p>
 *
 * <h2>Annotations Overview</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.annotation.Subscribe Subscribe}:
 *         Marks methods as event handlers that will be automatically invoked
 *         when a matching event occurs.</li>
 *     <li>{@link de.splatgames.aether.events.annotation.Warning Warning}:
 *         Used to specify warning behaviors for deprecated or potentially dangerous
 *         methods, constructors, or types.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <p>To register an event listener, annotate a method with {@link de.splatgames.aether.events.annotation.Subscribe}:</p>
 * <pre>{@code
 * public class MyEventListener implements Listener {
 *
 *     @Subscribe(priority = EventPriority.HIGH, ignoreCancelled = true)
 *     public void onCustomEvent(CustomEvent event) {
 *         System.out.println("Event received: " + event);
 *     }
 * }
 * }</pre>
 *
 * <p>Additionally, use {@link de.splatgames.aether.events.annotation.Warning Warning} to flag deprecated or experimental elements:</p>
 * <pre>{@code
 * @Warning(value = true, reason = "This method will be removed in a future version.")
 * public void oldMethod() {
 *     // Legacy logic
 * }
 * }</pre>
 *
 * <h2>Implementation Details</h2>
 * <ul>
 *     <li>The {@code @Subscribe} annotation is processed by the event bus,
 *         which detects and invokes registered handlers based on event type and priority.</li>
 *     <li>The {@code @Warning} annotation integrates with the warning system and can be
 *         used to control whether warnings are shown at runtime.</li>
 * </ul>
 *
 * <h2>Retention Policy</h2>
 * <p>All annotations in this package use {@code RetentionPolicy.RUNTIME},
 * ensuring they are available via reflection during execution.</p>
 *
 * @author Erik Pförtner
 * @see de.splatgames.aether.events.listener.Listener Listener
 * @see de.splatgames.aether.events.bus.EventBus EventBus
 * @since aether-events 1.0.0
 */
package de.splatgames.aether.events.annotation;

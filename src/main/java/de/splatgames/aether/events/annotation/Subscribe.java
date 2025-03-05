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

package de.splatgames.aether.events.annotation;

import de.splatgames.aether.events.event.EventPriority;
import de.splatgames.aether.events.listener.Listener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event listener in the Aether Event System.
 *
 * <p>Methods annotated with {@code @Subscribe} will be automatically detected
 * and invoked when an event of the corresponding type is posted to the event bus.</p>
 *
 * <h2>Usage</h2>
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
 * <p>To activate the listener, the containing class must implement {@link de.splatgames.aether.events.listener.Listener}
 * and be registered with the event bus using {@link de.splatgames.aether.events.bus.EventBus#subscribe(Listener) subscribe(Listener)}.</p>
 *
 * <h2>Priority Handling</h2>
 * <p>The {@link #priority()} value determines the order in which event handlers are called.</p>
 * <ul>
 *     <li>Higher priority values are executed first.</li>
 *     <li>Default priority is {@link de.splatgames.aether.events.event.EventPriority#NORMAL}.</li>
 * </ul>
 *
 * <h2>Ignoring Cancelled Events</h2>
 * <p>By default, cancelled events are still passed to all listeners. If {@link #ignoreCancelled()} is set to {@code true},
 * the method will not receive events that have been marked as cancelled.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    /**
     * Defines the execution priority of the event handler.
     *
     * <p>Higher priority handlers are executed before lower priority handlers.</p>
     *
     * @return the priority level of the event handler
     */
    EventPriority priority() default EventPriority.NORMAL;

    /**
     * Determines whether the event handler should ignore cancelled events.
     *
     * <p>If set to {@code true}, this handler will not receive events that have been cancelled.</p>
     *
     * @return {@code true} if cancelled events should be ignored, {@code false} otherwise
     */
    boolean ignoreCancelled() default false;
}

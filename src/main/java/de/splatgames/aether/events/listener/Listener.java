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

package de.splatgames.aether.events.listener;

/**
 * A marker interface for all event listeners in the Aether Event System.
 *
 * <p>Any class that wishes to receive events through the {@link de.splatgames.aether.events.bus.EventBus EventBus}
 * must implement this interface.
 * The presence of this interface allows the event bus to recognize
 * the class as a valid subscriber when calling {@link de.splatgames.aether.events.bus.EventBus#subscribe(Listener) subscribe(Listener)}.</p>
 *
 * <h2>Event Subscription</h2>
 * <p>To listen for events, a class must:</p>
 * <ul>
 *     <li>Implement {@code Listener}.</li>
 *     <li>Declare one or more methods annotated with {@link de.splatgames.aether.events.annotation.Subscribe Subscribe}.</li>
 *     <li>Register an instance of the class to the event bus using {@link de.splatgames.aether.events.bus.EventBus#subscribe(Listener) subscribe(Listener)}.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <p>A simple example of an event listener:</p>
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
 * <p>To activate the listener, it must be registered:</p>
 * <pre>{@code
 * EventBus eventBus = EventBus.builder().build();
 * eventBus.subscribe(new MyEventListener());
 * }</pre>
 *
 * <h2>Importance in Aether Event System</h2>
 * <p>This interface is a core part of the Aether Event System, as it ensures that only explicitly declared
 * listeners can receive events.
 * This prevents unintended method calls and allows precise control over
 * event handling.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public interface Listener {
}

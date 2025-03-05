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
 * Provides exception handling for the Aether Event System.
 * <p>
 * This package contains exceptions related to event processing failures. Exceptions
 * in this package are typically thrown when errors occur during event dispatching
 * or listener execution.
 * </p>
 *
 * <h2>Key Exception</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.event.exception.EventException EventException} – Thrown when an error
 *     occurs while processing an event.</li>
 * </ul>
 *
 * <h2>When Exceptions Occur</h2>
 * <p>Exceptions in this package may be thrown under the following circumstances:</p>
 * <ul>
 *     <li>An event handler throws an unexpected exception during execution.</li>
 *     <li>A problem occurs in the {@code EventBus} while dispatching an event.</li>
 *     <li>An event is in an invalid state, causing execution failure.</li>
 * </ul>
 *
 * <h2>Exception Handling</h2>
 * <p>To handle exceptions occurring during event execution, an exception handler
 * can be registered in the event bus:</p>
 * <pre>{@code
 * eventBus.setExceptionHandler((event, exception) -> {
 *     System.err.println("Error processing event: " + event.getEventName());
 *     exception.printStackTrace();
 * });
 * }</pre>
 *
 * <h2>Best Practices</h2>
 * <p>Developers should ensure that event handlers properly catch and log exceptions
 * where necessary. While the event bus provides centralized exception handling,
 * individual handlers should also handle errors appropriately to prevent failures
 * from propagating.</p>
 *
 * @author Erik Pförtner
 * @see de.splatgames.aether.events.event.exception.EventException EventException
 * @see de.splatgames.aether.events.bus.EventBus#setExceptionHandler(de.splatgames.aether.events.bus.EventBus.EventExceptionHandler) EventBus#setExceptionHandler(EventExceptionHandler)
 * @since aether-events 1.0.0
 */
package de.splatgames.aether.events.event.exception;

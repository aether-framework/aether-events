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

package de.splatgames.aether.events.event.exception;

import de.splatgames.aether.events.bus.EventBus;

import java.io.Serial;

/**
 * An exception thrown when an error occurs during event processing.
 *
 * <p>This exception is typically used within the event bus system to indicate
 * issues related to event handling, such as exceptions thrown by event listeners.</p>
 *
 * <h2>Causes</h2>
 * <p>An {@code EventException} may be thrown in various scenarios, including:</p>
 * <ul>
 *     <li>An event handler throws an exception during execution.</li>
 *     <li>An invalid event state is detected.</li>
 *     <li>Unexpected failures occur within the event dispatch system.</li>
 * </ul>
 *
 * <h2>Usage</h2>
 * <p>To handle exceptions thrown during event execution, an exception handler
 * can be registered via {@link de.splatgames.aether.events.bus.EventBus#setExceptionHandler(EventBus.EventExceptionHandler) setExceptionHandler(EventBus.EventExceptionHandler) }.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public class EventException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1509590370881975785L;

    /**
     * Constructs a new {@code EventException} with no detail message.
     */
    public EventException() {
    }

    /**
     * Constructs a new {@code EventException} with the specified detail message.
     *
     * @param message the detail message
     */
    public EventException(final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code EventException} with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public EventException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code EventException} with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public EventException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code EventException} with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message            the detail message
     * @param cause              the cause of the exception
     * @param enableSuppression  whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    public EventException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

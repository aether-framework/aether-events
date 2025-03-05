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

package de.splatgames.aether.events.bus;

import de.splatgames.aether.events.annotation.Warning;
import de.splatgames.aether.events.bus.impl.EventBusImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A builder for constructing instances of {@link EventBus}.
 *
 * <p>This builder allows customization of various {@code EventBus} parameters before instantiation.
 * It provides a fluent API for setting options such as logging behavior, warning states,
 * and global event handling configurations.</p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * EventBus eventBus = EventBus.builder()
 *     .withLogger(LoggerFactory.getLogger("CustomLogger"))
 *     .withUseTimings(true)
 *     .withThrowOnRegistered(false)
 *     .build();
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>This class is not thread-safe. It is intended to be used during a single-threaded
 * configuration phase before the {@code EventBus} is built.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public class EventBusBuilder {
    private Warning.WarningState warningState = Warning.WarningState.OFF;
    private boolean useTimings = false;
    private boolean throwOnRegistered = true;
    private Thread mainThread = Thread.currentThread();
    private Logger logger = LoggerFactory.getLogger(EventBus.class);
    private EventBus.EventInterceptor globalInterceptor = null;
    private EventBus.EventExceptionHandler globalExceptionHandler = null;

    /**
     * Constructs a new {@code EventBusBuilder} with default settings.
     *
     * <p>The following defaults are applied:</p>
     * <ul>
     *     <li>Warnings are turned off.</li>
     *     <li>Timings are disabled.</li>
     *     <li>Exceptions are thrown if a listener is already registered.</li>
     *     <li>The main thread is set to the current thread.</li>
     *     <li>Logging is handled by SLF4J using {@code LoggerFactory.getLogger(EventBus.class)}.</li>
     *     <li>No global event interceptor or exception handler is set.</li>
     * </ul>
     */
    protected EventBusBuilder() {

    }

    /**
     * Sets the warning state for the event bus.
     *
     * <p>The warning state controls how event-related warnings are handled.</p>
     *
     * @param warningState the desired warning state
     * @return this builder instance for chaining
     * @throws NullPointerException if {@code warningState} is {@code null}
     */
    public EventBusBuilder withWarningState(@NotNull final Warning.WarningState warningState) {
        this.warningState = warningState;
        return this;
    }

    /**
     * Enables or disables timing measurements for event execution.
     *
     * @param useTimings {@code true} to enable timing measurements, {@code false} to disable
     * @return this builder instance for chaining
     */
    public EventBusBuilder withUseTimings(final boolean useTimings) {
        this.useTimings = useTimings;
        return this;
    }

    /**
     * Determines whether an exception should be thrown if a listener is already registered.
     *
     * @param throwOnRegistered {@code true} to throw an exception when re-registering a listener,
     *                          {@code false} to allow multiple registrations
     * @return this builder instance for chaining
     */
    public EventBusBuilder withThrowOnRegistered(final boolean throwOnRegistered) {
        this.throwOnRegistered = throwOnRegistered;
        return this;
    }

    /**
     * Sets the main thread reference for the event bus.
     *
     * <p>This is primarily used for event dispatching in applications
     * where certain operations must run on a specific thread.</p>
     *
     * @param mainThread the thread to be considered as the main event processing thread
     * @return this builder instance for chaining
     */
    public EventBusBuilder withMainThread(final Thread mainThread) {
        this.mainThread = mainThread;
        return this;
    }

    /**
     * Sets the logger to be used by the event bus.
     *
     * @param logger the SLF4J logger instance to use
     * @return this builder instance for chaining
     * @throws NullPointerException if {@code logger} is {@code null}
     */
    public EventBusBuilder withLogger(@NotNull final Logger logger) {
        this.logger = logger;
        return this;
    }

    /**
     * Sets a global event interceptor for the event bus.
     *
     * <p>The interceptor can modify or filter events before they are delivered to listeners.
     * Setting this to {@code null} removes any previously set interceptor.</p>
     *
     * @param interceptor the event interceptor, or {@code null} to remove
     * @return this builder instance for chaining
     */
    @NotNull
    public EventBusBuilder withGlobalInterceptor(@Nullable final EventBus.EventInterceptor interceptor) {
        this.globalInterceptor = interceptor;
        return this;
    }

    /**
     * Sets a global exception handler for the event bus.
     *
     * <p>This handler is called when an exception occurs during event processing.
     * Setting this to {@code null} removes any previously set exception handler.</p>
     *
     * @param handler the exception handler, or {@code null} to remove
     * @return this builder instance for chaining
     */
    @NotNull
    public EventBusBuilder withGlobalExceptionHandler(@Nullable final EventBus.EventExceptionHandler handler) {
        this.globalExceptionHandler = handler;
        return this;
    }

    /**
     * Builds and returns a new {@code EventBus} instance with the configured settings.
     *
     * @return a new {@code EventBus} instance
     */
    public EventBus build() {
        return new EventBusImpl(this.warningState,
                this.useTimings,
                this.throwOnRegistered,
                this.mainThread,
                this.logger,
                this.globalInterceptor,
                this.globalExceptionHandler);
    }
}

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

import de.splatgames.aether.events.event.Event;
import de.splatgames.aether.events.listener.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A central event management system that allows event-based communication between components.
 * The {@code EventBus} provides mechanisms for event subscription, pipeline management,
 * and event dispatching.
 *
 * <p>Event listeners must implement the {@link de.splatgames.aether.events.listener.Listener}
 * interface and register themselves using {@link #subscribe(Listener)} to receive events.</p>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * EventBus eventBus = EventBus.builder().build();
 *
 * eventBus.subscribe(new MyEventListener());
 * EventPipeline<MyEvent> pipeline = eventBus.<MyEvent>createPipeline("custom:pipeline");
 *
 * pipeline
 *   .filter(event -> event.isImportant())
 *   .sorted(Comparator.comparing(MyEvent::getPriority))
 *   .registerConsumer(event -> System.out.println("Received event: " + event));
 *
 * pipeline.fire(new MyEvent("Hello, world!")); // This will run the listener's method
 * }</pre>
 *
 * <h2>Event Pipelines</h2>
 * <p>In addition to direct event posting, the event bus supports event pipelines,
 * allowing for advanced filtering and ordering of events:</p>
 * <pre>{@code
 * EventPipeline<CustomEvent> pipeline = eventBus.createPipeline("customPipeline");
 * pipeline.filter(event -> event.isImportant()).registerConsumer(System.out::println);
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>The event bus can be used in multi-threaded environments. If asynchronous event
 * handling is required, an {@link java.util.concurrent.ExecutorService} can be set
 * via {@link #setExecutorService(ExecutorService)}.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public interface EventBus {

    /**
     * Returns a new {@code EventBusBuilder} instance for constructing an {@code EventBus}.
     *
     * @return a new {@code EventBusBuilder}
     */
    @NotNull
    static EventBusBuilder builder() {
        return new EventBusBuilder();
    }

    /**
     * Subscribes a listener to receive events.
     *
     * <p>The listener must implement {@link Listener} and have methods annotated
     * with {@code @Subscribe} to receive events.</p>
     *
     * @param listener the listener to register
     * @throws NullPointerException if {@code listener} is {@code null}
     */
    void subscribe(@NotNull final Listener listener);

    /**
     * Unsubscribes a listener, preventing it from receiving further events.
     *
     * @param listener the listener to remove
     * @throws NullPointerException if {@code listener} is {@code null}
     */
    void unsubscribe(@NotNull final Listener listener);

    /**
     * Creates a named event pipeline for handling events of a specific type.
     *
     * @param <T>  the event type
     * @param name the name of the pipeline
     * @return a new event pipeline
     * @throws NullPointerException if {@code name} is {@code null}
     */
    @NotNull
    <T extends Event> EventPipeline<T> createPipeline(@NotNull final String name);

    /**
     * Retrieves an existing event pipeline by name.
     *
     * @param <T>  the event type
     * @param name the name of the pipeline
     * @return the pipeline, or {@code null} if not found
     * @throws NullPointerException if {@code name} is {@code null}
     */
    @Nullable
    <T extends Event> EventPipeline<T> getPipeline(@NotNull final String name);

    /**
     * Returns all registered event pipelines.
     *
     * @return a set of all event pipelines
     */
    @NotNull
    Set<EventPipeline<?>> getPipelines();

    /**
     * Returns a list of all currently registered listeners.
     *
     * @return a list of registered listeners
     */
    @NotNull
    List<Listener> getRegisteredListeners();

    /**
     * Sets a global event interceptor to modify or filter events before they are delivered.
     *
     * @param interceptor the event interceptor, or {@code null} to remove the interceptor
     */
    void setGlobalInterceptor(@Nullable final EventInterceptor interceptor);

    /**
     * Sets a custom exception handler for errors occurring during event dispatch.
     *
     * @param handler the exception handler, or {@code null} to use the default behavior
     */
    void setExceptionHandler(@Nullable final EventExceptionHandler handler);

    /**
     * Sets an {@link ExecutorService} for handling events asynchronously.
     *
     * @param executorService the executor service, or {@code null} to disable asynchronous execution
     */
    void setExecutorService(@Nullable final ExecutorService executorService);

    /**
     * Destroys the event bus, unsubscribing all listeners and cleaning up resources.
     */
    void destroy();

    /**
     * Destroys the event bus, with an option to force immediate shutdown.
     *
     * @param force {@code true} to force shutdown, {@code false} for graceful cleanup
     */
    void destroy(boolean force);

    /**
     * An interceptor that modifies or filters events before they are processed.
     */
    @FunctionalInterface
    interface EventInterceptor {

        /**
         * Intercepts an event before it is posted to listeners.
         *
         * @param event the event being posted
         * @return the modified event, or {@code null} to cancel the event
         */
        @Nullable
        Event intercept(@NotNull final Event event);
    }

    /**
     * Handles exceptions thrown during event dispatching.
     */
    @FunctionalInterface
    interface EventExceptionHandler {

        /**
         * Handles an exception that occurred while processing an event.
         *
         * @param event     the event that caused the exception
         * @param exception the exception that was thrown
         */
        void handle(@NotNull final Event event, @NotNull final Exception exception);
    }

    /**
     * A processing pipeline for filtering, sorting, and handling events.
     *
     * @param <T> the event type
     */
    interface EventPipeline<T extends Event> {

        /**
         * Adds a filter to the event pipeline.
         * Events that do not match the filter will be discarded.
         *
         * @param filter the predicate used for filtering
         * @return the current pipeline for chaining
         * @throws NullPointerException if {@code filter} is {@code null}
         */
        @NotNull
        EventPipeline<T> filter(@NotNull final Predicate<T> filter);

        /**
         * Specifies a sorting order for events in the pipeline.
         *
         * @param comparator the comparator to determine event order
         * @return the current pipeline for chaining
         * @throws NullPointerException if {@code comparator} is {@code null}
         */
        @NotNull
        EventPipeline<T> sorted(@NotNull final Comparator<T> comparator);

        /**
         * Fires an event, sending it through the pipeline to registered consumers.
         *
         * @param event the event to dispatch
         * @throws NullPointerException if {@code event} is {@code null}
         */
        void fire(@NotNull final T event);

        /**
         * Fires multiple events at once.
         *
         * @param events the list of events to dispatch
         * @throws NullPointerException if {@code events} is {@code null}
         */
        void fire(@NotNull final List<T> events);

        /**
         * Returns the name of the pipeline.
         *
         * @return the pipeline name
         */
        @NotNull
        String getName();

        /**
         * Registers a consumer that processes events in the pipeline.
         *
         * @param consumer the event consumer
         * @throws NullPointerException if {@code consumer} is {@code null}
         */
        void registerConsumer(@NotNull final Consumer<T> consumer);

        /**
         * Destroys the pipeline, removing all filters, consumers, and stored state.
         */
        void destroy();
    }
}

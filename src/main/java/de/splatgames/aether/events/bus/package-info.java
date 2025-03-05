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
 * Provides the core event management system for the Aether Event System.
 * <p>
 * This package contains the fundamental components required for event-driven
 * communication, including event subscription, dispatching, and pipeline management.
 * It enables a decoupled architecture where components can communicate without
 * direct dependencies.
 * </p>
 *
 * <h2>Key Components</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.bus.EventBus EventBus} – The central event dispatcher that
 *     manages event subscriptions and dispatching.</li>
 *     <li>{@link de.splatgames.aether.events.bus.EventBusBuilder EventBusBuilder} – A builder for configuring
 *     and constructing an {@code EventBus} instance.</li>
 * </ul>
 *
 * <h2>Basic Usage</h2>
 * <p>To create and use an {@link de.splatgames.aether.events.bus.EventBus EventBus}, instantiate it using the builder:</p>
 * <pre>{@code
 * EventBus eventBus = EventBus.builder()
 *     .withLogger(LoggerFactory.getLogger("CustomLogger"))
 *     .withUseTimings(true)
 *     .withThrowOnRegistered(false)
 *     .build();
 *
 * eventBus.subscribe(new MyEventListener());
 * eventBus.fire(new CustomEvent("Hello, world!"));
 * }</pre>
 *
 * <h2>Event Pipelines</h2>
 * <p>The event system includes an advanced event pipeline mechanism,
 * allowing developers to filter, sort, and manipulate event flows:</p>
 * <pre>{@code
 * EventPipeline<MyEvent> pipeline = eventBus.createPipeline("customPipeline");
 * pipeline.filter(event -> event.isImportant()).registerConsumer(System.out::println);
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>The event bus can be used in multi-threaded environments. If asynchronous event
 * handling is required, an {@link java.util.concurrent.ExecutorService ExecutorService} can be set
 * via {@link de.splatgames.aether.events.bus.EventBus#setExecutorService(java.util.concurrent.ExecutorService) EventBus#setExecutorService(ExecutorService)}.</p>
 *
 * <h2>Customization</h2>
 * <p>The event bus can be customized with several options:</p>
 * <ul>
 *     <li><b>Logging:</b> Set a custom logger via {@link de.splatgames.aether.events.bus.EventBusBuilder#withLogger(org.slf4j.Logger) EventBusBuilder#withLogger(Logger)}.</li>
 *     <li><b>Exception Handling:</b> Define a global exception handler using
 *     {@link de.splatgames.aether.events.bus.EventBus#setExceptionHandler(de.splatgames.aether.events.bus.EventBus.EventExceptionHandler) EventBus#setExceptionHandler(EventBus.EventExceptionHandler)}.</li>
 *     <li><b>Warning States:</b> Control warning behaviors with {@link de.splatgames.aether.events.bus.EventBusBuilder#withWarningState(de.splatgames.aether.events.annotation.Warning.WarningState) EventBusBuilder#withWarningState(Warning.WarningState)}.</li>
 * </ul>
 *
 * <h2>Retention Policy</h2>
 * <p>All core event components are designed for runtime usage, making them accessible
 * via reflection and dynamic event registration.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 * @see de.splatgames.aether.events.bus.EventBus EventBus
 * @see de.splatgames.aether.events.bus.EventBusBuilder EventBusBuilder
 * @see de.splatgames.aether.events.listener.Listener Listener
 * @see de.splatgames.aether.events.event.Event Event
 */
package de.splatgames.aether.events.bus;

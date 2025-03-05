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
 * Provides the internal implementation of the event bus system for the Aether Event System.
 * <p>
 * This package contains the concrete implementations of core event bus components.
 * It is designed to be used internally within the {@code aether-events} module and
 * should not be directly accessed by external consumers.
 * </p>
 *
 * <h2>Key Components</h2>
 * <ul>
 *     <li>{@link de.splatgames.aether.events.bus.impl.EventBusImpl EventBusImpl} – The concrete implementation
 *     of {@link de.splatgames.aether.events.bus.EventBus EventBus}, handling event dispatching,
 *     listener management, and pipeline execution.</li>
 *     <li>{@link de.splatgames.aether.events.bus.impl.EventPipelineImpl EventPipelineImpl} – The implementation of
 *     {@link de.splatgames.aether.events.bus.EventBus.EventPipeline EventPipeline}, allowing advanced
 *     event filtering, sorting, and execution management.</li>
 * </ul>
 *
 * <h2>Implementation Details</h2>
 * <p>The {@link de.splatgames.aether.events.bus.impl.EventBusImpl EventBusImpl} class maintains a set of registered listeners and event
 * pipelines, managing event dispatching while ensuring thread safety.</p>
 * <p>The {@link de.splatgames.aether.events.bus.impl.EventPipelineImpl EventPipelineImpl} class provides a structured way to process events
 * with filters and ordered execution.</p>
 *
 * <h2>Usage</h2>
 * <p>Instances of {@link de.splatgames.aether.events.bus.impl.EventBusImpl EventBusImpl} should be created via {@link de.splatgames.aether.events.bus.EventBusBuilder EventBusBuilder}
 * instead of being instantiated directly.</p>
 * <pre>{@code
 * EventBus eventBus = EventBus.builder().build();
 * eventBus.subscribe(new MyEventListener());
 * }</pre>
 *
 * <h2>Thread Safety</h2>
 * <p>{@link de.splatgames.aether.events.bus.impl.EventBusImpl EventBusImpl} is designed to support multi-threaded environments.
 * It allows asynchronous event processing through an {@link java.util.concurrent.ExecutorService ExecutorService}.
 * Pipelines ensure safe event dispatching, and the main event bus maintains a reference
 * to the application’s main thread.</p>
 *
 * <h2>Performance Considerations</h2>
 * <p>The implementation optimizes event dispatching using:</p>
 * <ul>
 *     <li><b>Concurrent HashMaps</b> – For efficient listener lookup and event mapping.</li>
 *     <li><b>Lazy Initialization</b> – Pipelines are created only when needed.</li>
 *     <li><b>Event Timing</b> – If enabled, execution times are logged for performance tracking.</li>
 * </ul>
 *
 * <h2>Retention Policy</h2>
 * <p>This package is an internal part of the Aether Event System and not intended
 * for direct public usage. All implementations are subject to change without notice.</p>
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 * @see de.splatgames.aether.events.bus.EventBus EventBus
 * @see de.splatgames.aether.events.bus.EventBus.EventPipeline EventPipeline
 */
package de.splatgames.aether.events.bus.impl;

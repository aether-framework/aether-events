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

package de.splatgames.aether.events.bus.impl;

import de.splatgames.aether.events.annotation.Subscribe;
import de.splatgames.aether.events.annotation.Warning;
import de.splatgames.aether.events.bus.EventBus;
import de.splatgames.aether.events.event.Event;
import de.splatgames.aether.events.event.EventExecutor;
import de.splatgames.aether.events.event.exception.EventException;
import de.splatgames.aether.events.listener.Listener;
import de.splatgames.aether.events.listener.RegisteredListener;
import de.splatgames.aether.events.util.EventTiming;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EventBusImpl implements EventBus {
    protected final Logger logger;
    private final Warning.WarningState warningState;
    private final boolean useTimings;
    private final boolean throwOnRegistered;
    private final Map<Class<? extends Event>, Set<RegisteredListener>> listeners = new ConcurrentHashMap<>();
    private final Map<String, EventPipeline<?>> pipelines = new ConcurrentHashMap<>();
    private final Map<String, LinkedList<EventTiming>> timings = new ConcurrentHashMap<>();
    private final Thread mainThread;
    private volatile EventInterceptor globalInterceptor;
    private volatile EventExceptionHandler exceptionHandler;
    private volatile ExecutorService executorService = Executors.newCachedThreadPool();

    public EventBusImpl(@NotNull final Warning.WarningState warningState,
                        final boolean useTimings,
                        final boolean throwOnRegistered,
                        @NotNull final Thread mainThread,
                        @NotNull final Logger logger,
                        @Nullable final EventInterceptor globalInterceptor,
                        @Nullable final EventExceptionHandler globalExceptionHandler) {
        this.warningState = warningState;
        this.useTimings = useTimings;
        this.throwOnRegistered = throwOnRegistered;
        this.mainThread = mainThread;
        this.logger = logger;
        this.globalInterceptor = globalInterceptor;
        this.exceptionHandler = globalExceptionHandler;
    }

    @Override
    public void subscribe(@NotNull final Listener listener) {
        this.listeners.putAll(this.createListener(listener));
    }

    @Override
    public void unsubscribe(@NotNull final Listener listener) {
        for (final Set<RegisteredListener> listeners : this.listeners.values()) {
            listeners.removeIf(registeredListener -> registeredListener.listener().equals(listener));
        }
    }

    @NotNull
    @Override
    public <T extends Event> EventPipeline<T> createPipeline(@NotNull final String name) {
        if (this.pipelines.containsKey(name)) {
            if (this.throwOnRegistered) {
                throw new IllegalArgumentException("Pipeline " + name + " already defined in scope!");
            } else {
                // only log a warning and overwrite the existing pipeline
                this.logger.warn("Pipeline {} already defined in scope!", name);
            }
        }
        EventPipelineImpl<T> pipeline = new EventPipelineImpl<>(name, this);
        this.pipelines.put(name, pipeline);
        return pipeline;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T extends Event> EventPipeline<T> getPipeline(@NotNull final String name) {
        try {
            return (EventPipeline<T>) this.pipelines.get(name);
        } catch (final ClassCastException ex) {
            throw new IllegalArgumentException("Pipeline " + name + " is not of the correct type!", ex);
        }
    }

    @NotNull
    @Override
    public Set<EventPipeline<?>> getPipelines() {
        return new HashSet<>(this.pipelines.values());
    }

    @NotNull
    @Override
    public List<Listener> getRegisteredListeners() {
        return this.listeners.values().stream()
                .flatMap(Set::stream)
                .map(RegisteredListener::listener)
                .toList();
    }

    @Override
    public void setGlobalInterceptor(@Nullable final EventInterceptor interceptor) {
        this.globalInterceptor = interceptor;
    }

    @Override
    public void setExceptionHandler(@Nullable final EventExceptionHandler handler) {
        this.exceptionHandler = handler;
    }

    @Override
    public void setExecutorService(final @Nullable ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void destroy() {
        this.destroy(false);
    }

    @Override
    public void destroy(final boolean force) {
        if (this.executorService != null) {
            if (force) {
                this.executorService.shutdownNow();
            } else {
                this.executorService.shutdown();
            }
        }

        for (EventPipeline<?> pipeline : this.pipelines.values()) {
            pipeline.destroy();
        }

        this.pipelines.clear();
        this.listeners.clear();
    }

    protected <T extends Event> void registerConsumer(@NotNull final EventPipelineImpl<T> eventPipeline, @NotNull final Consumer<T> consumer) {
        eventPipeline.filter(event -> {
            consumer.accept(event);
            return false;
        });
    }

    protected <T extends Event> void destroyPipeline(final EventPipelineImpl<T> eventPipeline) {
        this.pipelines.remove(eventPipeline.getName());
    }

    protected <T extends Event> boolean isDestroyed(final EventPipeline<T> eventPipeline) {
        return !this.pipelines.containsKey(eventPipeline.getName());
    }

    protected <T extends Event> void fire(@NotNull final T event, @NotNull final String pipeline) {
        Set<RegisteredListener> listeners = this.listeners.get(event.getClass());
        if (listeners == null || listeners.isEmpty()) {
            return;
        }

        if (event.isAsync()) {
            if (Thread.holdsLock(this)) {
                throw new IllegalStateException(event.getEventName() + " cannot be fired asynchronously from inside synchronized code.");
            }
            if (Thread.currentThread() == this.mainThread) {
                throw new IllegalStateException(event.getEventName() + " cannot be fired asynchronously from the main thread.");
            }
        } else {
            if (Thread.currentThread() != this.mainThread) {
                throw new IllegalStateException(event.getEventName() + " cannot be fired synchronously from an asynchronous thread.");
            }
        }

        if (this.globalInterceptor != null) {
            try {
                Event interceptedEvent = this.globalInterceptor.intercept(event);
                if (interceptedEvent == null) {
                    this.logger.debug("Event {} was intercepted and cancelled.", event.getEventName());
                    return;
                }
            } catch (final Throwable ex) {
                this.logger.error("Could not pass event {} to global interceptor", event.getEventName(), ex);
            }
        }

        EventTiming timing = null;
        if (this.useTimings) {
            timing = new EventTiming(event.getEventName());
        }

        this.fire(listeners, event, pipeline);

        if (this.useTimings) {
            timing.stop();

            LinkedList<EventTiming> timingList = this.timings.computeIfAbsent(pipeline, k -> new LinkedList<>());
            timingList.add(timing);

            if (timingList.size() > 100) {
                timingList.removeFirst();
            }

            final int eventCount = timingList.size();
            if (eventCount >= 10 && eventCount % 10 == 0) {
                double avgMillis = timingList.stream()
                        .mapToLong(EventTiming::getDurationNano)
                        .average()
                        .orElse(0) / 1_000_000.0;

                this.logger.info("Average time for {} over the last {} events: {}ms",
                        pipeline, Math.min(eventCount, 100), String.format("%.3f", avgMillis));
            }

        }
    }

    private void fire(@NotNull final Set<RegisteredListener> listeners, @NotNull final Event event, @NotNull final String pipeline) {
        for (final RegisteredListener registration : listeners) {
            try {
                registration.callEvent(event, (this.executorService != null && !Objects.equals(this.executorService, Executors.newCachedThreadPool())) ? this.executorService : null);
            } catch (final EventException ex) {
                if (this.exceptionHandler != null) {
                    this.exceptionHandler.handle(event, ex);
                } else {
                    this.logger.error("Could not pass event {} to {} on pipeline {}", event.getEventName(), registration.listener().getClass().getName(), pipeline, ex);
                }
            }
        }
    }

    @NotNull
    public Map<Class<? extends Event>, Set<RegisteredListener>> createListener(@NotNull final Listener listener) {
        Map<Class<? extends Event>, Set<RegisteredListener>> returning = new HashMap<>();
        Set<Method> methods;

        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            Collections.addAll(methods, publicMethods);
            Collections.addAll(methods, privateMethods);
        } catch (final NoClassDefFoundError e) {
            return returning;
        }

        for (final Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                final Subscribe subscriber = method.getAnnotation(Subscribe.class);
                if (subscriber == null) {
                    this.logger.error("Method {} has @Subscribe annotation but no Subscriber annotation", method);
                    continue;
                }
                if (method.isBridge() || method.isSynthetic()) {
                    this.logger.error("Method {} is bridge/synthetic and weird", method);
                    continue;
                }
                final Class<?> checked;
                if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checked = method.getParameterTypes()[0])) {
                    this.logger.error("Method {} has @Subscribe annotation but takes wrong type of argument", method);
                    continue;
                }
                final Class<? extends Event> event = checked.asSubclass(Event.class);
                method.setAccessible(true);
                Set<RegisteredListener> listeners = returning.computeIfAbsent(event, k -> new HashSet<>());
                for (Class<?> clazz = event; Event.class.isAssignableFrom(clazz); clazz = clazz.getSuperclass()) {
                    if (clazz.getAnnotation(Deprecated.class) != null) {
                        Warning warning = clazz.getAnnotation(Warning.class);
                        if (!this.warningState.printFor(warning)) {
                            break;
                        }

                        this.logger.warn("\"{}\" has registered a listener for {} on method \"{}\", but the event is Deprecated. \"{}\"!",
                                listener.getClass().getName(),
                                clazz.getName(),
                                method.toGenericString(),
                                (warning != null && !warning.reason().isEmpty()) ? warning.reason() : "Application performance will be affected");
                        if (this.warningState == Warning.WarningState.ON) {
                            this.logger.warn("Stack trace of the event registration: ", new Exception());
                        }
                        break;
                    }
                }

                EventExecutor eventExecutor = (localListener, localEvent) -> {
                    try {
                        if (!event.isAssignableFrom(localEvent.getClass())) {
                            return;
                        }

                        method.invoke(localListener, localEvent);
                    } catch (final InvocationTargetException exception) {
                        throw new EventException(exception.getCause());
                    } catch (final Throwable throwable) {
                        throw new EventException(throwable);
                    }
                };

                listeners.add(new RegisteredListener(listener, eventExecutor, subscriber.priority(), this, subscriber.ignoreCancelled()));
            }
        }

        return returning;
    }
}

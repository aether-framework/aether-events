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

import de.splatgames.aether.events.bus.EventBus;
import de.splatgames.aether.events.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventPipelineImpl<T extends Event> implements EventBus.EventPipeline<T> {
    private final String name;
    private final EventBusImpl eventBus;
    private final List<Predicate<T>> filters = new ArrayList<>();
    private Comparator<T> comparator = null;

    public EventPipelineImpl(@NotNull final String name, @NotNull final EventBusImpl eventBus) {
        this.name = name;
        this.eventBus = eventBus;
    }

    @NotNull
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void registerConsumer(@NotNull final Consumer<T> consumer) {
        this.eventBus.registerConsumer(this, consumer);
    }

    @Override
    public void destroy() {
        if (this.eventBus.isDestroyed(this)) {
            return;
        }

        this.filters.clear();
        this.eventBus.destroyPipeline(this);
    }

    @NotNull
    @Override
    public EventBus.EventPipeline<T> filter(@NotNull final Predicate<T> filter) {
        this.filters.add(filter);
        return this;
    }

    /**
     * Specifies a comparator to sort events in the pipeline.
     *
     * @param comparator the {@link Comparator} used to define the sorting order of events; must not be null.
     * @return the current instance of the event pipeline with the sorting behavior applied.
     * @apiNote This method only applies to the {@link #fire(List)} method
     * and won't affect the order of events fired using the {@link #fire(Event)} method.
     */
    @NotNull
    @Override
    public EventBus.EventPipeline<T> sorted(@NotNull final Comparator<T> comparator) {
        this.comparator = comparator;
        return this;
    }

    @Override
    public void fire(@NotNull final T event) {
        if (this.eventBus.isDestroyed(this)) {
            this.eventBus.logger.warn("Pipeline {} is destroyed, cannot fire event {}", this.name, event);
            return;
        }

        for (Predicate<T> filter : this.filters) {
            if (!filter.test(event)) {
                return;
            }
        }

        this.eventBus.fire(event, name);
    }

    @Override
    public void fire(@NotNull final List<T> events) {
        if (this.eventBus.isDestroyed(this)) {
            this.eventBus.logger.warn("Pipeline {} is destroyed, cannot fire events {}", this.name, events);
            return;
        }

        if (this.comparator != null && events.size() > 1) {
            events.sort(this.comparator);
        }

        for (T event : events) {
            fire(event);
        }
    }
}

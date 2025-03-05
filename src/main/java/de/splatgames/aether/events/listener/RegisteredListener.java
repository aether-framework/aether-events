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

import de.splatgames.aether.events.bus.impl.EventBusImpl;
import de.splatgames.aether.events.event.Cancellable;
import de.splatgames.aether.events.event.Event;
import de.splatgames.aether.events.event.EventExecutor;
import de.splatgames.aether.events.event.EventPriority;
import de.splatgames.aether.events.event.exception.EventException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

public record RegisteredListener(Listener listener, EventExecutor eventExecutor, EventPriority priority,
                                 EventBusImpl eventBus, boolean ignoringCancellation) {
    public RegisteredListener(@NotNull final Listener listener,
                              @NotNull final EventExecutor eventExecutor,
                              @NotNull final EventPriority priority,
                              @NotNull final EventBusImpl eventBus,
                              final boolean ignoringCancellation) {
        this.listener = listener;
        this.eventExecutor = eventExecutor;
        this.priority = priority;
        this.eventBus = eventBus;
        this.ignoringCancellation = ignoringCancellation;
    }

    @Override
    @NotNull
    public Listener listener() {
        return listener;
    }

    @Override
    @NotNull
    public EventExecutor eventExecutor() {
        return eventExecutor;
    }

    @Override
    @NotNull
    public EventPriority priority() {
        return priority;
    }

    @Override
    @NotNull
    public EventBusImpl eventBus() {
        return eventBus;
    }

    public void callEvent(@NotNull final Event event, @Nullable final ExecutorService executorService) throws EventException {
        if (event instanceof Cancellable) {
            if (((Cancellable) event).isCancelled() && ignoringCancellation()) {
                return;
            }
        }

        if (event.isAsync() && executorService != null) {
            executorService.execute(() -> eventExecutor.execute(listener, event));
        } else {
            eventExecutor.execute(listener, event);
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final RegisteredListener that = (RegisteredListener) obj;
        return ignoringCancellation == that.ignoringCancellation &&
                listener.equals(that.listener) &&
                eventExecutor.equals(that.eventExecutor) &&
                priority == that.priority &&
                eventBus.equals(that.eventBus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listener, eventExecutor, priority, eventBus, ignoringCancellation);
    }
}

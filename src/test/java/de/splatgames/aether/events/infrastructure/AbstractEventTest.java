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

package de.splatgames.aether.events.infrastructure;

import de.splatgames.aether.events.bus.EventBus;
import de.splatgames.aether.events.infrastructure.event.DynamicEvent;
import de.splatgames.aether.events.infrastructure.listener.TestListener;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.UUID;

public abstract class AbstractEventTest {

    protected EventBus eventBus;

    @BeforeEach
    protected void setUp() {
        eventBus = EventBus.builder().build();
    }

    @AfterEach
    protected void tearDown() {
        if (eventBus != null) {
            eventBus.destroy();
            eventBus = null;
        }
    }

    protected void dispatchAndAssert(@NotNull final DynamicEvent event) {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("dynamic:" + event.getClass().getSimpleName() + ":" + UUID.randomUUID());
        dispatchAndAssert(event, pipeline);
    }

    protected void dispatchAndAssert(@NotNull final DynamicEvent event, @NotNull final EventBus.EventPipeline<DynamicEvent> pipeline) {
        TestListener listener = new TestListener();
        eventBus.subscribe(listener);

        Assertions.assertThat(eventBus.getRegisteredListeners()
                .stream()
                .anyMatch(l -> l == listener))
                .as("Listener should be registered")
                .isTrue();

        pipeline.fire(event);

        try {
            boolean received = listener.awaitEvent(100);
            Assertions.assertThat(received)
                    .as("Event should have been received")
                    .isTrue();
            Assertions.assertThat(listener.getLastEvent())
                    .as("Listener should have received the correct event")
                    .isEqualTo(event);
        } catch (InterruptedException e) {
            Assertions.fail("Test was interrupted", e);
        }
    }

    protected void assertNoEventReceived(@NotNull final DynamicEvent event) {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("dynamic:" + event.getClass().getSimpleName() + ":" + UUID.randomUUID());
        assertNoEventReceived(event, pipeline);
    }

    protected void assertNoEventReceived(@NotNull final DynamicEvent event, @NotNull final EventBus.EventPipeline<DynamicEvent> pipeline) {
        TestListener listener = new TestListener();
        eventBus.subscribe(listener);

        pipeline.fire(event);

        try {
            boolean received = listener.awaitEvent(100);
            Assertions.assertThat(received)
                    .as("Event should NOT have been received")
                    .isFalse();
        } catch (InterruptedException e) {
            Assertions.fail("Test was interrupted", e);
        }
    }
}

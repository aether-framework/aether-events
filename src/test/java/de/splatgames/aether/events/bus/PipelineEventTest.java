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

import de.splatgames.aether.events.infrastructure.AbstractEventTest;
import de.splatgames.aether.events.infrastructure.event.DynamicEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

class PipelineEventTest extends AbstractEventTest {

    @Test
    void testPipelineName() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        Assertions.assertThat(pipeline.getName()).isEqualTo("TestPipeline");
    }

    @Test
    void testRegisterConsumer() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        AtomicBoolean processed = new AtomicBoolean(false);
        pipeline.registerConsumer(event -> processed.set(true));

        DynamicEvent event = new DynamicEvent();
        pipeline.fire(event);

        Assertions.assertThat(processed.get())
                .as("Consumer should have processed the event")
                .isTrue();
    }

    @Test
    void testFireEvent() {
        DynamicEvent event = new DynamicEvent();

        dispatchAndAssert(event);
    }

    @Test
    void testFilterBlocksEvent() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        pipeline.filter(e -> false); // Blocks all events

        DynamicEvent event = new DynamicEvent();
        assertNoEventReceived(event, pipeline);
    }

    @Test
    void testFilterAllowsEvent() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        pipeline.filter(e -> true); // Allows all events

        DynamicEvent event = new DynamicEvent();
        dispatchAndAssert(event, pipeline);
    }

    @Test
    void testFireEventAfterDestroy() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        pipeline.destroy();

        DynamicEvent event = new DynamicEvent();
        assertNoEventReceived(event, pipeline);
    }

    @Test
    void testSortingOrderApplied() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        pipeline.sorted(Comparator.comparing(e -> Objects.requireNonNull(e.getAttribute("order", Integer.class))));

        DynamicEvent event1 = new DynamicEvent(Map.of("order", 2));
        DynamicEvent event2 = new DynamicEvent(Map.of("order", 1));
        DynamicEvent event3 = new DynamicEvent(Map.of("order", 3));

        List<DynamicEvent> events = Arrays.asList(event1, event2, event3);
        pipeline.fire(events);

        // Sorts events by the 'order' attribute
        Assertions.assertThat(events)
                .as("Events should be sorted by the 'order' attribute")
                .containsExactly(event2, event1, event3);
    }

    @Test
    void testDestroyPipelineClearsFilters() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        pipeline.filter(e -> true);
        pipeline.destroy();

        Assertions.assertThat(pipeline.filter(event -> false)).isNotNull();
    }

    @Test
    void testCannotFireOnDestroyedPipeline() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        pipeline.destroy();

        DynamicEvent event = new DynamicEvent();
        pipeline.fire(event);

        assertNoEventReceived(event, pipeline);
    }

    @Test
    void testMultipleFilters() {
        EventBus.EventPipeline<DynamicEvent> pipeline = eventBus.createPipeline("TestPipeline");
        
        Predicate<DynamicEvent> filter1 = e -> e.hasAttribute("allowed");
        Predicate<DynamicEvent> filter2 = e -> Objects.equals(e.getAttribute("allowed"), true);
        
        pipeline.filter(filter1).filter(filter2);

        DynamicEvent blockedEvent = new DynamicEvent();
        DynamicEvent allowedEvent = new DynamicEvent(Map.of("allowed", true));

        assertNoEventReceived(blockedEvent, pipeline);
        dispatchAndAssert(allowedEvent, pipeline);
    }
}

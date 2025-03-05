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

package de.splatgames.aether.events.infrastructure.listener;

import de.splatgames.aether.events.annotation.Subscribe;
import de.splatgames.aether.events.event.Event;
import de.splatgames.aether.events.infrastructure.event.DynamicEvent;
import de.splatgames.aether.events.listener.Listener;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestListener implements Listener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestListener.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private DynamicEvent lastEvent = null;

    @Subscribe
    public void onEvent(final DynamicEvent event) {
        this.lastEvent = event;
        latch.countDown();
        LOGGER.info("Received event: {}", event);
    }

    public boolean awaitEvent(final long timeoutMillis) throws InterruptedException {
        return latch.await(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    @Nullable
    public DynamicEvent getLastEvent() {
        return lastEvent;
    }
}

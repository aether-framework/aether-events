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

package de.splatgames.aether.events.infrastructure.event;

import de.splatgames.aether.events.event.Cancellable;
import de.splatgames.aether.events.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * A highly modular and dynamic event that can carry arbitrary attributes.
 * This allows flexible event handling while maintaining structured event metadata.
 *
 * @author Erik Pförtner
 * @since aether-events 1.0.0
 */
public class DynamicEvent extends Event implements Cancellable {

    private final UUID eventId;
    private final Instant timestamp;
    private final Map<String, Object> attributes;
    private boolean cancelled = false;

    /**
     * Creates a new dynamic event with an empty attribute map.
     */
    public DynamicEvent() {
        this.eventId = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.attributes = new HashMap<>();
    }

    /**
     * Creates a dynamic event with predefined attributes.
     *
     * @param initialAttributes A map of attributes to initialize the event with.
     */
    public DynamicEvent(@NotNull final Map<String, Object> initialAttributes) {
        this.eventId = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.attributes = new HashMap<>(initialAttributes);
    }

    /**
     * Gets the unique event ID.
     *
     * @return The unique identifier of this event.
     */
    public @NotNull UUID getEventId() {
        return eventId;
    }

    /**
     * Gets the event timestamp.
     *
     * @return The timestamp when the event was created.
     */
    public @NotNull Instant getTimestamp() {
        return timestamp;
    }

    /**
     * Sets a dynamic attribute on the event.
     *
     * @param key   The attribute key.
     * @param value The value to set.
     */
    public void setAttribute(@NotNull final String key, @Nullable final Object value) {
        attributes.put(key, value);
    }

    /**
     * Retrieves an attribute from the event.
     *
     * @param key The key to fetch.
     * @return The attribute value, or {@code null} if not set.
     */
    public @Nullable Object getAttribute(@NotNull final String key) {
        return attributes.get(key);
    }

    /**
     * Retrieves an attribute with a specific type.
     *
     * @param key  The attribute key.
     * @param type The expected return type.
     * @param <T>  The generic type.
     * @return The casted attribute, or {@code null} if not set.
     */
    public <T> @Nullable T getAttribute(@NotNull final String key, @NotNull final Class<T> type) {
        Object value = attributes.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }

    /**
     * Checks if an attribute exists.
     *
     * @param key The key to check.
     * @return {@code true} if the attribute is present.
     */
    public boolean hasAttribute(@NotNull final String key) {
        return attributes.containsKey(key);
    }

    /**
     * Returns an unmodifiable view of the attributes.
     *
     * @return A read-only map of event attributes.
     */
    public @NotNull Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Cancels the event, preventing further processing.
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * Checks if the event has been cancelled.
     *
     * @return {@code true} if the event is cancelled.
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Executes a consumer if the event is NOT cancelled.
     *
     * @param action The consumer action.
     */
    public void ifNotCancelled(@NotNull final Consumer<DynamicEvent> action) {
        if (!cancelled) {
            action.accept(this);
        }
    }

    @Override
    public @NotNull String toString() {
        return "DynamicEvent{" +
                "eventId=" + eventId +
                ", timestamp=" + timestamp +
                ", attributes=" + attributes +
                ", cancelled=" + cancelled +
                '}';
    }
}

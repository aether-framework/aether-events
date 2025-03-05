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

package de.splatgames.aether.events.annotation;

import de.splatgames.aether.events.util.MapUtil;
import de.splatgames.aether.events.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Indicates whether warnings should be issued for the annotated element.
 * <p>
 * This annotation can be applied to methods, constructors, or types to specify
 * whether warnings related to the annotated element should be displayed. This is
 * particularly useful for marking deprecated or potentially dangerous elements.
 * </p>
 *
 * <h2>Usage Examples:</h2>
 * <ul>
 *      <li>Enable warnings for a method:</li>
 *      <pre>{@code
 *      @Warning(value = true, reason = "This method is deprecated.")
 *      public void someDeprecatedMethod() { ... }
 *      }</pre>
 *      <li>Disable warnings for a type:</li>
 *      <pre>{@code
 *      @Warning(value = false)
 *      public class ExperimentalFeature { ... }
 *      }</pre>
 * </ul>
 *
 * @see Warning.WarningState
 * @see Deprecated
 */
@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Warning {

    enum WarningState {

        /**
         * Indicates all warnings should be printed for deprecated items.
         */
        ON,
        /**
         * Indicates no warnings should be printed for deprecated items.
         */
        OFF,
        /**
         * Indicates each warning would default to the configured {@link
         * Warning} annotation, or always if annotation not found.
         */
        DEFAULT;

        private static final Map<String, WarningState> values = MapUtil.of(
                Pair.of("off", OFF),
                Pair.of("false", OFF),
                Pair.of("f", OFF),
                Pair.of("no", OFF),
                Pair.of("n", OFF),
                Pair.of("on", ON),
                Pair.of("true", ON),
                Pair.of("t", ON),
                Pair.of("yes", ON),
                Pair.of("y", ON),
                Pair.of("", DEFAULT),
                Pair.of("d", DEFAULT),
                Pair.of("default", DEFAULT)
        );

        /**
         * This method checks the provided warning should be printed for this
         * state
         *
         * @param warning The warning annotation added to a deprecated item
         * @return <ul>
         * <li>ON is always True
         * <li>OFF is always false
         * <li>DEFAULT is false if and only if annotation is not null and
         * specifies false for {@link Warning#value()}, true otherwise.
         * </ul>
         */
        public boolean printFor(@Nullable Warning warning) {
            if (this == DEFAULT) {
                return warning == null || warning.value();
            }
            return this == ON;
        }

        /**
         * This method returns the corresponding warning state for the given
         * string value.
         *
         * @param value The string value to check
         * @return {@link #DEFAULT} if not found, or the respective
         * WarningState
         */
        @NotNull
        public static WarningState value(@Nullable final String value) {
            if (value == null) {
                return DEFAULT;
            }
            WarningState state = values.get(value.toLowerCase());
            if (state == null) {
                return DEFAULT;
            }
            return state;
        }
    }

    boolean value() default false;

    String reason() default "";
}

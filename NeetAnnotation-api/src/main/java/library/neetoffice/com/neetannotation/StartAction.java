package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * The annotation method will be cell after service start.
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface StartAction {
    /**
     * @return Intent action
     */
    String value() default "";

    /**
     * @return StartResult
     */
    int returnValue() default 0;

    /**
     * The annotation value is the key used for extra. If not set, the field or method name will be used as the key.
     */
    @Retention(SOURCE)
    @Target(PARAMETER)
    @interface Extra {
        /**
         * Bundle key
         *
         * @return key
         */
        String value() default "";
    }
}

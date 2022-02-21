package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Annotation field will be save instance in activity and fragment.
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface SaveInstance {
    /**
     * Bundle key
     *
     * @return key
     */
    String value() default "";
}

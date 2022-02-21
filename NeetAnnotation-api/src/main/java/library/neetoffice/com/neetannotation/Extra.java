package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * The annotation value is the key used for extra. If not set, the field or method name will be used as the key.
 * */
@Retention(SOURCE)
@Target(FIELD)
public @interface Extra {
    /**
     * Bundle key
     *
     * @return key
     */
    String value() default "";
}

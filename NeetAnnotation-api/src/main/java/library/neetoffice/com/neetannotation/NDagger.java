package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @deprecated
 */
@Retention(SOURCE)
@Target(TYPE)
@Deprecated
public @interface NDagger {

    /**
     * modules is module for Dagger2 to add Component
     *
     * @return module class array
     */
    Class<?>[] modules() default {};
}

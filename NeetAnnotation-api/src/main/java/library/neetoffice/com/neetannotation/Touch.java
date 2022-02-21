package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Methods annotated with @{@link TouchDown} will be called on touch up event
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Touch {
    /**
     * Same resName
     *
     * @return Resource name.
     */
    String[] value() default {};

    /**
     * Resource name
     *
     * @return Resource name.
     */
    String[] resName() default {};

    /**
     * Resource package.
     *
     * @return Resource package.
     */
    String resPackage() default "";

    /**
     * Touch action.
     */
    @Retention(RUNTIME)
    @Target(FIELD)
    @interface Action {
    }

    /**
     * Touch index.
     */
    @Retention(RUNTIME)
    @Target(FIELD)
    @interface Index {
    }
}

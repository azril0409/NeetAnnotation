package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Methods annotated with @{@link AfterTextChange} will be called on after text change event
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface AfterTextChange {
    /**
     * Same resName
     *
     * @return Resource name.
     */
    String[] value() default {};

    /**
     * Resource name.
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
}

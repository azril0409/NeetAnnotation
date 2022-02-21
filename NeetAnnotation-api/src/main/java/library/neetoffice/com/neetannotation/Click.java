package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with @{@link Click} will be called on click event
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Click {
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
}

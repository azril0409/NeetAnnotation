package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods annotated with @{@link CheckedChange} will be called on checked change event
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckedChange {
    /**
     * Same resName
     *
     * @return Resource name.
     */
    String[] value() default {};

    /**
     * Same resName
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

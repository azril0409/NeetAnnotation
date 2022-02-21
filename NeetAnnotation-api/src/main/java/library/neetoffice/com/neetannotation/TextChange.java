package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Methods annotated with @{@link TextChange} will be called on text change event
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface TextChange {
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
     * The before text.
     */
    @Retention(RUNTIME)
    @Target(PARAMETER)
    @interface Before {
    }

    /**
     * The after text.
     */
    @Retention(RUNTIME)
    @Target(PARAMETER)
    @interface After {
    }
}

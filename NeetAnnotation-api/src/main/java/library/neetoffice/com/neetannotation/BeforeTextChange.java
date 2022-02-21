package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Methods annotated with @{@link BeforeTextChange} will be called on before text change event
 * */
@Retention(SOURCE)
@Target(METHOD)
public @interface BeforeTextChange {
    /**
     * Same resName
     * @return Resource name.
     * */
    String[] value() default {};
    /**
     * Same resName
     * @return Resource name.
     * */
    String[] resName() default {};
    /**
     * Resource package.
     * @return Resource package.
     * */
    String resPackage() default "";
}

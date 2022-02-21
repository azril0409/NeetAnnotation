package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Inject by options menu.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface OptionsMenu {
    /**
     * Same resName
     *
     * @return Resource name.
     */
    String value() default "";

    /**
     * Resource name
     *
     * @return Resource name.
     */
    String resName() default "";

    /**
     * Resource package.
     *
     * @return Resource package.
     */
    String resPackage() default "";
}

package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Inject by options menu item.
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface OptionsMenuItem {
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

package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject resource boolean value by resource name.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface ResBoolean {
    /**
     * Same then resName
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

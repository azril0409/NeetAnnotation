package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject resource drawable value by resource name.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface ResDrawable {
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

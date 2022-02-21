package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inject fragment by resource name or fragment tag.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface FragmentBy {
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

    /**
     * Inject by fragment tag.
     *
     * @return Fragment tag.
     */
    String tag() default "";
}

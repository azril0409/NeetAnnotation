package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Should be used on {@link NView} classes to enable usage of NeetAnnotations.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface NView {

    /**
     * Same viewBinding.
     *
     * @return viewBinding class.
     */
    Class value() default NoBinder.class;

    /**
     * ViewBinding class.
     *
     * @return viewBinding class.
     */
    Class viewBinding() default NoBinder.class;

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

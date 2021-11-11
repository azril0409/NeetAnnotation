package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target(TYPE)
public @interface NActivity {

    Class value() default NoBinder.class;

    Class viewBinding() default NoBinder.class;

    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the layout
     */
    String resName() default "";
    String resPackage() default "";
}

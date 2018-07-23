package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target(TYPE)

public @interface NFragment {

    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the layout
     */
    int value() default 0;


    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the layout
     */
    String resName() default "";
}

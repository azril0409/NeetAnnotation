package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo on 2019/1/9.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface FragmentBy {

    /**
     * The R.id.* field which refer to the layout.
     *
     * @return the id of the R.id
     */
    int id() default 0;


    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the R.id
     */
    String resName() default "";


    String tag() default "";
}

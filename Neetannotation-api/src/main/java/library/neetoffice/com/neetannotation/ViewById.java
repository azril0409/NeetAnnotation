package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo on 2016/3/17.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface ViewById {

    /**
     * The R.id.* field which refer to the layout.
     *
     * @return the id of the R.id
     */
    int value() default 0;


    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the R.id
     */
    String resName() default "";
}

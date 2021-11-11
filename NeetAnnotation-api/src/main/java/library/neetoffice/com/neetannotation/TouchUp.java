package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo on 2016/4/8.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TouchUp {
    /**
     * The R.layout.* field which refer to the layout.
     *
     * @return the id of the R.id
     */
    String[] value() default {};
    String[] resName() default {};
    String resPackage() default "";
}

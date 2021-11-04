package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Deo-chainmeans on 2017/11/13.
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface ResIntArray {
    String value() default "";

    String resName() default "";
}

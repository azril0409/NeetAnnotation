package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Deo on 2016/3/18.
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface Touch {
    int[] value() default {};

    String[] resName() default {};

    @Retention(RUNTIME)
    @Target(FIELD)
    @interface Action {
    }

    @Retention(RUNTIME)
    @Target(FIELD)
    @interface Index {
    }
}

package library.neetoffice.com.neetannotation;

import android.app.Activity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mac on 2016/04/04.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnActivityResult {

    int value();

    int resultCode() default Activity.RESULT_OK;

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER})
    public @interface Extra {
        String value();
    }
}

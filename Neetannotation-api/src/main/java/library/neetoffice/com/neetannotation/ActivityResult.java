package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target(METHOD)
public @interface ActivityResult {

    int value();

    int resultCode() default -1;

    @Retention(SOURCE)
    @Target({PARAMETER})
    @interface Extra {
        String value() default "";
    }
}

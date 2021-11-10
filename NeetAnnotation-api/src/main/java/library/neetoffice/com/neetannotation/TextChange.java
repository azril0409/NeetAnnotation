package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@Target(METHOD)
public @interface TextChange {

    String[] value() default {};

    String[] resName() default {};

    @Retention(RUNTIME)
    @Target(PARAMETER)
    @interface Before {
    }

    @Retention(RUNTIME)
    @Target(PARAMETER)
    @interface After {
    }
}

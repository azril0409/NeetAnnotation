package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Identify name of NeetAnnotations
 * */
@Target({METHOD, FIELD})
@Retention(SOURCE)
public @interface NamedAs {
    /**
     * @return name
     * */
    String[] value() default {};
}

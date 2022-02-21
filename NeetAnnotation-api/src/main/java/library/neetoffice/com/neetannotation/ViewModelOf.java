package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Inject viewModelOf.
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface ViewModelOf {
    /**
     * @return Key
     * */
    String value() default "";
}

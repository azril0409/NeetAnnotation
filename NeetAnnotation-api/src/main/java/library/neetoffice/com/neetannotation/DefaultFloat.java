package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The default value of the preference
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface DefaultFloat {
    /**
     * The default value of the preference.
     *
     * @return the default value
     */
    float value();
}

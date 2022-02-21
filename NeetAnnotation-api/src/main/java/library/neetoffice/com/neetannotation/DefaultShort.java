package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * The default value of the preference
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface DefaultShort {
    /**
     * The default value of the preference.
     *
     * @return the default value
     */
    short value();
}

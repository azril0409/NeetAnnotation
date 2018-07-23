package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Deo-chainmeans on 2017/5/20.
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

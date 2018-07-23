package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by Deo-chainmeans on 2017/5/20.
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface DefaultLong {
    /**
     * The default value of the preference.
     *
     * @return the default value
     */
    long value();
}

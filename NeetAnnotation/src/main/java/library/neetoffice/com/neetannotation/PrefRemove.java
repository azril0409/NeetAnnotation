package library.neetoffice.com.neetannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo-chainmeans on 2017/5/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PrefRemove {
    /**
     * The R.string.* field which refers to the key of the preference.
     *
     * @return the resource name of the preference key
     */
    int[] keyRes() default {};
}

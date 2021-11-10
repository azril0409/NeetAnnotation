package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Target({METHOD, FIELD})
@Retention(SOURCE)
public @interface Subscribe {
    /**
     * @return the Class of items observed and emitted by the Published
     */
    Class<?> viewmode();

    /**
     * @return this key for ViewModelProviders of key
     */
    String key() default "";
}

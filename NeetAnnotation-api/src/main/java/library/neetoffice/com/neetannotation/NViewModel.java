package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Should be used on {@link NViewModel} classes to enable usage of NeetAnnotations.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface NViewModel {
    /**
     * The viewmodel is single object in app.
     *
     * @return Boolean
     */
    boolean isSingle() default false;
}

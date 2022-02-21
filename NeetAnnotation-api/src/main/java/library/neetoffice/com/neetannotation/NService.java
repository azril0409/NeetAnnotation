package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Should be used on {@link NService} classes to enable usage of NeetAnnotations.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface NService {
}

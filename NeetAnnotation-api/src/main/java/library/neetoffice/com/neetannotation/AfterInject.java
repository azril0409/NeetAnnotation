package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Methods annotated with @{@link AfterInject} will be called on init or onCreate
 * <pre>
 * &#064;NActivity()
 * public class MyActivity extends Activity {
 *  &#064;AfterInject()
 *  void onAfterInject(Bundle bundle) {
 *  }
 * }
 * </pre>
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface AfterInject {
}

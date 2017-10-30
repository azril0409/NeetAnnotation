package library.neetoffice.com.neetannotation;

import android.content.Context;

/**
 * Created by Deo-chainmeans on 2017/10/30.
 */

public abstract class BeanHelp {
    public static <T> T newInstance(Context context, Class<T> beanClass) {
        final NBean k = beanClass.getAnnotation(NBean.class);
        Object h;
        if (k != null && k.value() == NBean.Scope.Singleton) {
            h = BindField.newStaticInstance(beanClass, context);
        } else {
            h = BindField.newInstance(beanClass, context);
        }


        return (T) h;
    }
}

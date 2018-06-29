package library.neetoffice.com.neetannotation.viewmodesupport;

import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;
import library.neetoffice.com.neetannotation.AnnotationUtil;

/**
 * Created by azril on 2018/2/21.
 */

public class NeetConsumer implements Consumer {
    final Object a;
    final Method b;

    public NeetConsumer(Object a, Method b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void accept(Object o) throws Exception {
        AnnotationUtil.invoke(b, a, o);
    }
}

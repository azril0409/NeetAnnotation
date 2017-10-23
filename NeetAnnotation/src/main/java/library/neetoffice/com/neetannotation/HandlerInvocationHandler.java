package library.neetoffice.com.neetannotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Handler;

/**
 * Created by Deo-chainmeans on 2017/9/5.
 */

public class HandlerInvocationHandler implements InvocationHandler {
    private final Handler h = new Handler();
    private final Object o;

    public HandlerInvocationHandler(Object object) {
        this.o = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Method m = o.getClass().getMethod(method.getName(), method.getParameterTypes());
        if (m == null) {
            return null;
        }
        final Background b = m.getAnnotation(Background.class);
        final UIThread u = m.getAnnotation(UIThread.class);
        if (b != null) {
            new Thread(new DoRunnable(o, m, args)).start();
            return null;
        } else if (u != null) {
            h.postDelayed(new DoRunnable(o, m, args), u.delayMillis());
            return null;
        } else {
            return m.invoke(o, args);
        }
    }

    private static class DoRunnable implements Runnable {
        private final Object o;
        private final Method m;
        private final Object[] args;

        private DoRunnable(Object object, Method method, Object[] args) {
            this.o = object;
            this.m = method;
            this.args = args;
        }

        @Override
        public void run() {
            try {
                AnnotationUtil.invoke(m, o, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
};

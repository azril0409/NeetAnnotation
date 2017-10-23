package library.neetoffice.com.neetannotation;

import android.os.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Deo on 2016/4/1.
 */
public abstract class EventBus {
    private static final Handler HANDLER = new Handler();
    private static final HashMap<Object, HashMap<Class<?>, ArrayList<Method>>> MAP = new HashMap<>();

    public static void register(Object subscriber) {
        synchronized (MAP) {
            if (MAP.containsKey(subscriber)) {
                return;
            }
            final Class<?> c = subscriber.getClass();
            final HashMap<Class<?>, ArrayList<Method>> hashMap = new HashMap<>();
            final Method[] a = c.getDeclaredMethods();
            for (Method b : a) {
                final Subscribe d = b.getAnnotation(Subscribe.class);
                if (d != null) {
                    final Class<?>[] e = b.getParameterTypes();
                    if (e.length != 1) {
                        throw new AnnotationException(b.getName() + " neet one parameter");
                    }
                    final Enforce enforce = d.value();
                    if (hashMap.containsKey(e[0])) {
                        ArrayList<Method> methods = hashMap.get(e[0]);
                        methods.add(b);
                    } else {
                        ArrayList<Method> methods = new ArrayList<>();
                        methods.add(b);
                        hashMap.put(e[0], methods);
                    }
                }
            }
            MAP.put(subscriber, hashMap);
        }
    }

    public static void unregister(Object subscriber) {
        synchronized (MAP) {
            MAP.remove(subscriber);
        }
    }

    public static void post(Object event) {
        synchronized (MAP) {
            final Class<?> c = event.getClass();
            for (Object a : MAP.keySet()) {
                final HashMap<Class<?>, ArrayList<Method>> b = MAP.get(a);
                if (b.containsKey(c)) {
                    ArrayList<Method> d = b.get(c);
                    for (Method f : d) {
                        final Subscribe s = f.getAnnotation(Subscribe.class);
                        final Enforce e = s.value();
                        if (Enforce.UIThread == e) {
                            HANDLER.post(new Task(f, a, event));
                        } else if (Enforce.Background == e) {
                            new Thread(new Task(f, a, event)).start();
                        }
                    }
                }
            }
        }
    }

    private static class Task implements Runnable {
        final Method method;
        final Object object;
        final Object event;

        private Task(Method method, Object object, Object event) {
            this.method = method;
            this.object = object;
            this.event = event;
        }


        @Override
        public void run() {
            try {
                AnnotationUtil.invoke(method,object, event);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}

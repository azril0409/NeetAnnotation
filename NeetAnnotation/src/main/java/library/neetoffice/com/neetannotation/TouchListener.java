package library.neetoffice.com.neetannotation;

import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by Deo on 2016/3/18.
 */
class TouchListener implements View.OnTouchListener {
    static class A {
        int d;
        Method b;

        A(int d, Method b) {
            this.d = d;
            this.b = b;
        }
    }

    final Object a;
    final HashMap<Integer, A> b = new HashMap<>();
    final HashMap<Integer, A> c = new HashMap<>();
    final HashMap<Integer, A> d = new HashMap<>();
    final HashMap<Integer, A> f = new HashMap<>();

    TouchListener(Object a) {
        this.a = a;
    }

    private A createA(Method b, String n) {
        final int d;
        final Class<?>[] c = b.getParameterTypes();
        if (c.length == 0) {
            d = 0;
        } else if (c.length == 1) {
            if (View.class.isAssignableFrom(c[0])) {
                d = 1;
            } else if (MotionEvent.class.isAssignableFrom(c[0])) {
                d = 2;
            } else {
                throw new AnnotationException(b.getName() + " neet  no parameter or View , MotionEvent parameter");
            }
        } else if (c.length == 2) {
            if (View.class.isAssignableFrom(c[0]) && MotionEvent.class.isAssignableFrom(c[1])) {
                d = 3;
            } else if (MotionEvent.class.isAssignableFrom(c[0]) && View.class.isAssignableFrom(c[1])) {
                d = 4;
            } else {
                throw new AnnotationException(b.getName() + " neet  no parameter or View , MotionEvent parameter");
            }
        } else {
            throw new AnnotationException(b.getName() + " neet  no parameter or View , MotionEvent parameter");
        }
        return new A(d, b);
    }

    void addTouch(int i, Method b) {
        this.b.put(i, createA(b, Touch.class.getSimpleName()));
    }

    void addTouchDown(int i, Method b) {
        this.c.put(i, createA(b, TouchDown.class.getSimpleName()));
    }

    void addTouchMove(int i, Method b) {
        this.d.put(i, createA(b, TouchMove.class.getSimpleName()));
    }

    void addTouchUp(int i, Method b) {
        this.f.put(i, createA(b, TouchUp.class.getSimpleName()));
    }

    @Override
    public boolean onTouch(View v, MotionEvent h) {
        boolean r = false;
        if (MotionEvent.ACTION_DOWN == h.getAction()) {
            if (b.containsKey(v.getId())) {
                r = r | doA(b.get(v.getId()), v, h);
            }
            if (c.containsKey(v.getId())) {
                r = r | doA(c.get(v.getId()), v, h);
            }
        } else if (MotionEvent.ACTION_MOVE == h.getAction()) {
            if (b.containsKey(v.getId())) {
                r = r | doA(b.get(v.getId()), v, h);
            }
            if (d.containsKey(v.getId())) {
                r = r | doA(d.get(v.getId()), v, h);
            }
        } else if (MotionEvent.ACTION_UP == h.getAction()) {
            if (b.containsKey(v.getId())) {
                r = r | doA(b.get(v.getId()), v, h);
            }
            if (f.containsKey(v.getId())) {
                r = r | doA(f.get(v.getId()), v, h);
            }
        }
        return r;
    }

    private boolean doA(A a, View v, MotionEvent event) {
        final int d = a.d;
        final Method b = a.b;
        if (d == 0) {
            try {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a);
                    if (e != null) {
                        return (boolean) e;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 1) {
            try {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a, v);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, v);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, v);
                    if (e != null) {
                        return (boolean) e;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 2) {
            try {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a, event);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, event);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, event);
                    if (e != null) {
                        return (boolean) e;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 3) {
            try {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a, v, event);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, v, event);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, v, event);
                    if (e != null) {
                        return (boolean) e;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else if (d == 4) {
            try {
                if (b.getReturnType() == void.class) {
                    AnnotationUtil.invoke(b, a, event, v);
                } else if (b.getReturnType() == boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, event, v);
                    return (boolean) e;
                } else if (b.getReturnType() == Boolean.class) {
                    final Object e = AnnotationUtil.invoke(b, a, event, v);
                    if (e != null) {
                        return (boolean) e;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

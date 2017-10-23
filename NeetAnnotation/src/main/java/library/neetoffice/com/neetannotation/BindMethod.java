package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo on 2016/3/18.
 */
abstract class BindMethod {
    static void bindClick(Activity a, Method c) {
        final Click d = c.getAnnotation(Click.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = a.findViewById(f);
            if (g == null) {
                continue;
            }
            g.setOnClickListener(new ClickListener(a, c));
        }
    }

    static void bindClick(Object a, View b, Method c) {
        final Click d = c.getAnnotation(Click.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = b.findViewById(f);
            if (g == null) {
                continue;
            }
            g.setOnClickListener(new ClickListener(a, c));
        }
    }

    static void bindLongClick(Activity a, Method c) {
        final LongClick d = c.getAnnotation(LongClick.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = a.findViewById(f);
            if (g == null) {
                continue;
            }
            g.setOnLongClickListener(new LongClickListener(a, c));
        }
    }

    static void bindLongClick(Object a, View b, Method c) {
        final LongClick d = c.getAnnotation(LongClick.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = b.findViewById(f);
            if (g == null) {
                continue;
            }
            g.setOnLongClickListener(new LongClickListener(a, c));
        }
    }

    static void bindTouch(Activity a, Method c, TouchListener l) {
        final Touch d = c.getAnnotation(Touch.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = a.findViewById(f);
            if (g == null) {
                continue;
            }
            l.addTouch(f, c);
            g.setOnTouchListener(l);
        }
    }

    static void bindTouch(Object a, View b, Method c, TouchListener l) {
        final Touch d = c.getAnnotation(Touch.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            l.addTouch(g, c);
            h.setOnTouchListener(l);
        }
    }

    static void bindTouchDown(Activity a, Method c, TouchListener l) {
        final TouchDown d = c.getAnnotation(TouchDown.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = a.findViewById(f);
            if (g == null) {
                continue;
            }
            l.addTouchDown(f, c);
            g.setOnTouchListener(l);
        }
    }

    static void bindTouchDown(Object a, View b, Method c, TouchListener l) {
        final TouchDown d = c.getAnnotation(TouchDown.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            l.addTouchDown(g, c);
            h.setOnTouchListener(l);
        }
    }

    static void bindTouchMove(Activity a, Method c, TouchListener l) {
        final TouchMove d = c.getAnnotation(TouchMove.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = a.findViewById(f);
            if (g == null) {
                continue;
            }
            l.addTouchMove(f, c);
            g.setOnTouchListener(l);
        }
    }

    static void bindTouchMove(Object a, View b, Method c, TouchListener l) {
        final TouchMove d = c.getAnnotation(TouchMove.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            l.addTouchMove(g, c);
            h.setOnTouchListener(l);
        }
    }

    static void bindTouchUp(Activity a, Method c, TouchListener l) {
        final TouchUp d = c.getAnnotation(TouchUp.class);
        if (d == null) {
            return;
        }
        final int[] e = d.value();
        for (int f : e) {
            final View g = a.findViewById(f);
            if (g == null) {
                continue;
            }
            l.addTouchUp(f, c);
            g.setOnTouchListener(l);
        }
    }

    static void bindTouchUp(Object a, View b, Method c, TouchListener l) {
        final TouchUp d = c.getAnnotation(TouchUp.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            l.addTouchUp(g, c);
            h.setOnTouchListener(l);
        }
    }

    static void bindItemClick(Activity a, Method c) {
        final ItemClick d = c.getAnnotation(ItemClick.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = a.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof AdapterView) {
                ((AdapterView) h).setOnItemClickListener(new ItemClickListener(a, c));
            }
        }
    }

    static void bindItemLongClick(Activity a, Method c) {
        final ItemLongClick d = c.getAnnotation(ItemLongClick.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = a.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof AdapterView) {
                ((AdapterView) h).setOnItemLongClickListener(new ItemLongClickListener(a, c));
            }
        }
    }

    static void bindItemClick(Object a, View b, Method c) {
        final ItemClick d = c.getAnnotation(ItemClick.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof AdapterView) {
                ((AdapterView) h).setOnItemClickListener(new ItemClickListener(a, c));
            }
        }
    }

    static void bindItemLongClick(Object a, View b, Method c) {
        final ItemLongClick d = c.getAnnotation(ItemLongClick.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof AdapterView) {
                ((AdapterView) h).setOnItemLongClickListener(new ItemLongClickListener(a, c));
            }
        }
    }

    static void bindCheckedChange(Activity a, Method c) {
        final CheckedChange d = c.getAnnotation(CheckedChange.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = a.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof CompoundButton) {
                ((CompoundButton) h).setOnCheckedChangeListener(new CheckedChangeListener(a, c));
            }
        }
    }

    static void bindCheckedChange(Object a, View b, Method c) {
        final CheckedChange d = c.getAnnotation(CheckedChange.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof CompoundButton) {
                ((CompoundButton) h).setOnCheckedChangeListener(new CheckedChangeListener(a, c));
            }
        }
    }

    static void bindTextChange(Activity a, Method c) {
        final TextChange d = c.getAnnotation(TextChange.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = a.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof TextView) {
                ((TextView) h).addTextChangedListener(new TextChangeListener(h, a, c));
            }
        }
    }

    static void bindTextChange(Object a, View b, Method c) {
        final TextChange d = c.getAnnotation(TextChange.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof TextView) {
                ((TextView) h).addTextChangedListener(new TextChangeListener(h, a, c));
            }
        }
    }

    static void bindFocusChange(Activity a, Method c) {
        final FocusChange d = c.getAnnotation(FocusChange.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = a.findViewById(g);
            if (h == null) {
                continue;
            }
            h.setOnFocusChangeListener(new FocusChangeListener(a, c));
        }
    }

    static void bindFocusChange(Object a, View b, Method c) {
        final FocusChange d = c.getAnnotation(FocusChange.class);
        if (d == null) {
            return;
        }
        final int[] f = d.value();
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            h.setOnFocusChangeListener(new FocusChangeListener(a, c));
        }
    }

    static void onActivityResult(Object a, int b, int t, Intent c) {
        Class<?> d = a.getClass();
        do {
            final NActivity q = d.getAnnotation(NActivity.class);
            final NFragment r = d.getAnnotation(NFragment.class);
            if (q != null || r != null) {
                final Method[] f = d.getDeclaredMethods();
                for (Method g : f) {
                    final OnActivityResult h = g.getAnnotation(OnActivityResult.class);
                    if (h == null) {
                        continue;
                    }
                    if (h.value() == b) {
                        if (h.resultCode() != t) {
                            continue;
                        }
                        Class<?>[] i = g.getParameterTypes();
                        final Object[] o = new Object[i.length];
                        final Annotation[][] j = g.getParameterAnnotations();
                        for (int k = 0; k < i.length; k++) {
                            Annotation[] m = j[k];
                            OnActivityResult.Extra n = findParameterAnnotation(j[k], OnActivityResult.Extra.class);
                            Class<?> s = i[k];
                            if (s == Intent.class) {
                                o[k] = c;
                            } else if (s == Bundle.class && c != null) {
                                o[k] = c.getExtras();
                            } else if (n != null && c != null && c.getExtras() != null) {
                                o[k] = c.getExtras().get(n.value());
                            }
                        }
                        try {
                            AnnotationUtil.invoke(g, a, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            d = d.getSuperclass();
        } while (d != null);
    }

    static <T> T findParameterAnnotation(Annotation[] a, Class<T> cls) {
        T n = null;
        for (Annotation p : a) {
            if (p.annotationType() == cls) {
                n = (T) p;
                break;
            }
        }
        return n;
    }

    static boolean isAfterAnnotationMethod(Method a) {
        final AfterAnnotation b = a.getAnnotation(AfterAnnotation.class);
        if (b == null) {
            return false;
        }
        final Class<?>[] c = a.getParameterTypes();
        if (c.length == 0) {
            return true;
        } else if (c.length == 1 && c[0].isAssignableFrom(Bundle.class)) {
            return true;
        } else {
            final Exception exception = new AnnotationException(a.getName() + " neet  () or (Bundle)");
            exception.printStackTrace();
            return false;
        }
    }
}

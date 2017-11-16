package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Context;
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
    static int[] findResourcesID(int[] a, String b, String c, Context d) {
        if (a.length != 0) {
            return a;
        }
        if (b.toLowerCase().endsWith(c.toLowerCase())) {
            return new int[]{FindResources.id(d, b.substring(0, b.length() - c.length()))};
        }
        return new int[]{FindResources.id(d, b)};
    }

    static void bindClick(Object a, View b, Method c, Context d) {
        final Click e = c.getAnnotation(Click.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "Clicked", d);
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            h.setOnClickListener(new ClickListener(a, c));
        }
    }

    static void bindLongClick(Object a, View b, Method c, Context d) {
        final LongClick e = c.getAnnotation(LongClick.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "LongClicked", d);
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            h.setOnLongClickListener(new LongClickListener(a, c));
        }
    }

    static void bindTouch(Object a, View b, Method c, TouchListener d, Context e) {
        final Touch f = c.getAnnotation(Touch.class);
        if (f == null) {
            return;
        }
        final int[] g = findResourcesID(f.value(), c.getName(), "Touched", e);
        for (int h : g) {
            final View i = b.findViewById(h);
            if (i == null) {
                continue;
            }
            d.addTouch(h, c);
            i.setOnTouchListener(d);
        }
    }

    static void bindTouchDown(Object a, View b, Method c, TouchListener d, Context e) {
        final TouchDown f = c.getAnnotation(TouchDown.class);
        if (f == null) {
            return;
        }
        final int[] g = findResourcesID(f.value(), c.getName(), "TouchDowned", e);
        for (int h : g) {
            final View i = b.findViewById(h);
            if (i == null) {
                continue;
            }
            d.addTouchDown(h, c);
            i.setOnTouchListener(d);
        }
    }

    static void bindTouchMove(Object a, View b, Method c, TouchListener d, Context e) {
        final TouchMove f = c.getAnnotation(TouchMove.class);
        if (f == null) {
            return;
        }
        final int[] g = findResourcesID(f.value(), c.getName(), "TouchMoved", e);
        for (int h : g) {
            final View i = b.findViewById(h);
            if (i == null) {
                continue;
            }
            d.addTouchMove(h, c);
            i.setOnTouchListener(d);
        }
    }

    static void bindTouchUp(Object a, View b, Method c, TouchListener d, Context e) {
        final TouchUp f = c.getAnnotation(TouchUp.class);
        if (f == null) {
            return;
        }
        final int[] g = findResourcesID(f.value(), c.getName(), "TouchUpped", e);
        for (int h : g) {
            final View i = b.findViewById(h);
            if (i == null) {
                continue;
            }
            d.addTouchUp(h, c);
            i.setOnTouchListener(d);
        }
    }

    static void bindItemClick(Object a, View b, Method c, Context d) {
        final ItemClick e = c.getAnnotation(ItemClick.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "ItemClicked", d);
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

    static void bindItemLongClick(Object a, View b, Method c, Context d) {
        final ItemLongClick e = c.getAnnotation(ItemLongClick.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "ItemLongClicked", d);
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

    static void bindItemSelectClick(Object a, View b, Method c, Context d) {
        final ItemSelect e = c.getAnnotation(ItemSelect.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "ItemSelected", d);
        for (int g : f) {
            final View h = b.findViewById(g);
            if (h == null) {
                continue;
            }
            if (h instanceof AdapterView) {
                ((AdapterView) h).setOnItemSelectedListener(new ItemSelectedListener(a, c));
            }
        }
    }

    static void bindCheckedChange(Object a, View b, Method c, Context d) {
        final CheckedChange e = c.getAnnotation(CheckedChange.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "CheckedChanged", d);
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

    static void bindTextChange(Object a, View b, Method c, Context d) {
        final TextChange e = c.getAnnotation(TextChange.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "TextChanged", d);
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


    static void bindFocusChange(Object a, View b, Method c, Context d) {
        final FocusChange e = c.getAnnotation(FocusChange.class);
        if (e == null) {
            return;
        }
        final int[] f = findResourcesID(e.value(), c.getName(), "FocusChanged", d);
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
                        final Class<?>[] i = g.getParameterTypes();
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
            final Exception exception = new AnnotationException(a.getName() + " need  () or (Bundle)");
            exception.printStackTrace();
            return false;
        }
    }
}

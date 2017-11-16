package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Deo-chainmeans on 2017/10/23.
 */

abstract class BindBase {
    static void baseFieldBind(Object a, Field b, Context c) {
        BindField.bindRootContext(a, b, c);
        BindField.bindApp(a, b, c);
        BindField.bindBean(a, b, c);
        BindField.bindResString(a, b, c);
        BindField.bindResStringArray(a, b, c);
        BindField.bindResBoolean(a, b, c);
        BindField.bindResDimen(a, b, c);
        BindField.bindResInteger(a, b, c);
        BindField.bindResIntArray(a, b, c);
        BindField.bindResColor(a, b, c, c.getTheme());
        BindField.bindResDrawable(a, b, c, c.getTheme());
        BindField.bindResAnimation(a, b, c);
        BindField.bindResLayoutAnimation(a, b, c);
        BindField.bindSharedPreferences(a, b, c);
        BindField.bindHandler(a, b, c);
        BindField.bindSystemService(a, b, c);
        BindRestService.bind(a, b, c);
    }

    static void baseListenerBind(Activity a, Method b, TouchListener c) {
        BindMethod.bindClick(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindLongClick(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindTouch(a, a.getWindow().getDecorView(), b, c, a);
        BindMethod.bindTouchDown(a, a.getWindow().getDecorView(), b, c, a);
        BindMethod.bindTouchMove(a, a.getWindow().getDecorView(), b, c, a);
        BindMethod.bindTouchUp(a, a.getWindow().getDecorView(), b, c, a);
        BindMethod.bindItemClick(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindItemLongClick(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindItemSelectClick(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindCheckedChange(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindFocusChange(a, a.getWindow().getDecorView(), b, a);
        BindMethod.bindTextChange(a, a.getWindow().getDecorView(), b, a);
    }

    static void baseViewListenerBind(Object a, View b, Method c, TouchListener d, Context f) {
        BindMethod.bindClick(a, b, c, f);
        BindMethod.bindLongClick(a, b, c, f);
        BindMethod.bindTouch(a, b, c, d, f);
        BindMethod.bindTouchDown(a, b, c, d, f);
        BindMethod.bindTouchMove(a, b, c, d, f);
        BindMethod.bindTouchUp(a, b, c, d, f);
        BindMethod.bindItemClick(a, b, c, f);
        BindMethod.bindItemLongClick(a, b, c, f);
        BindMethod.bindCheckedChange(a, b, c, f);
        BindMethod.bindFocusChange(a, b, c, f);
        BindMethod.bindTextChange(a, b, c, f);
    }

    static void callAfterAnnotationMethod(Object a, Method b, Bundle c) {
        try {
            final Class<?>[] l = b.getParameterTypes();
            if (l.length == 1 && Bundle.class.isAssignableFrom(l[0])) {
                AnnotationUtil.invoke(b, a, c);
            } else if (l.length == 0) {
                AnnotationUtil.invoke(b, a);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

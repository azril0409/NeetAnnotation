package library.neetoffice.com.neetannotation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Created by Deo on 2016/3/8.
 */
public class AnnotationHelp {

    public static void onCreate(Application application) {
        BindApplication.onCreate(application);
    }

    public static void init(ViewGroup view) {
        BindView.onCreate(view);
    }

    public static void onCreate(Object object, Bundle savedInstanceState) {
        if (object instanceof Activity) {
            BindActivity.onCreate((Activity) object, savedInstanceState);
        } else if (object instanceof Service) {
            BindService.onCreate((Service) object);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && object instanceof android.app.Fragment) {
            BindFragment.onCreate((Fragment) object, savedInstanceState);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(object.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupportFragment");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getMethod("onCreate", new Class[]{sf, Bundle.class});
                    m.invoke(null, object, savedInstanceState);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public static void onSaveInstanceState(Object object, Bundle outState) {
        if (object instanceof Activity) {
            BindActivity.onSaveInstanceState((Activity) object, outState);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && object instanceof android.app.Fragment) {
            BindFragment.onSaveInstanceState((Fragment) object, outState);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(object.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupportFragment");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getMethod("onSaveInstanceState", new Class[]{sf, Bundle.class});
                    m.invoke(null, object, outState);
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public static void onActivityResult(Object object, int requestCode, int resultCode, Intent data) {
        BindMethod.onActivityResult(object, requestCode, resultCode, data);
    }

    public static boolean onCreateOptionsMenu(Object object, MenuInflater menuInflater, Menu menu) {
        return BindMenu.onCreateOptionsMenu(object, menuInflater, menu);
    }

    public static boolean onOptionsItemSelected(Object object, MenuItem menu) {
        return BindMenu.onOptionsItemSelected(object, menu);
    }

    public static View onCreateView(Object fragment, ViewGroup container, Bundle savedInstanceState) {
        if (fragment instanceof Fragment) {
            return BindFragment.onCreateView((Fragment) fragment, container, savedInstanceState);
        } else {
            try {
                final Class<?> sf = Class.forName("android.support.v4.app.Fragment");
                if (sf.isAssignableFrom(fragment.getClass())) {
                    final Class<?> bsf = Class.forName("library.neetoffice.com.neetannotation.BindSupportFragment");
                    if (bsf == null) {
                        throw new AnnotationException("No compile NeetAnnotationSupport");
                    }
                    final Method m = bsf.getMethod("onCreateView", new Class[]{sf, ViewGroup.class, Bundle.class});
                    return (View) m.invoke(null, fragment, container, savedInstanceState);
                } else {
                    return new View(((Fragment) fragment).getActivity());
                }
            } catch (Exception e) {
                throw new AnnotationException(e);
            }
        }
    }

    public static void onReceive(BroadcastReceiver broadcastReceiver, Context context, Intent intent) {
        BindBroadcastReceiver.onReceive(broadcastReceiver, context, intent);
    }

    public static void onStartCommand(Service service, Intent intent) {
        BindService.onStartCommand(service, intent);
    }
}
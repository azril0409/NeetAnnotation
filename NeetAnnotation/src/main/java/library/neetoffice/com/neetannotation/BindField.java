package library.neetoffice.com.neetannotation;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.job.JobScheduler;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.HashMap;

/**
 * Created by Deo on 2016/3/18.
 */
abstract class BindField {
    private static final HashMap<String, Object> MAP = new HashMap<>();

    static Object newInstance(Class<?> a, Context c) {
        try {
            final Constructor g = a.getDeclaredConstructor(new Class[]{Context.class});
            g.setAccessible(true);
            return g.newInstance(c);
        } catch (NoSuchMethodException e) {
            try {
                return a.newInstance();
            } catch (InstantiationException e1) {
                throw new AnnotationException(a.getSimpleName() + " neet  no-arg or Context parameter");
            } catch (IllegalAccessException e1) {
                throw new AnnotationException(a.getSimpleName() + " neet  no-arg or Context parameter");
            }
        } catch (IllegalAccessException e) {
            throw new AnnotationException(a.getSimpleName() + " neet  no-arg or Context parameter");
        } catch (InstantiationException e) {
            throw new AnnotationException(a.getSimpleName() + " neet  no-arg or Context parameter");
        } catch (InvocationTargetException e) {
            throw new AnnotationException(a.getSimpleName() + " neet  no-arg or Context parameter");
        }
    }

    static Object newStaticInstance(Class<?> a, Context c) {
        if (MAP.containsKey(a.getName())) {
            return MAP.get(a.getName());
        } else {
            final Object b = newInstance(a, c);
            MAP.put(a.getName(), b);
            return b;
        }
    }

    static void bindBean(Object a, Field b, Context c) {
        final Bean d = b.getAnnotation(Bean.class);
        if (d == null) {
            return;
        }
        Class<?> f = b.getType();
        Object h = BeanHelp.newInstance(c, f);
        try {
            AnnotationUtil.set(b, a, h);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void bindRootContext(Object a, Field b, Context c) {
        final RootContext d = b.getAnnotation(RootContext.class);
        if (d == null) {
            return;
        }
        final Class<?> f = b.getType();
        if (Context.class.isAssignableFrom(f)) {
            try {
                AnnotationUtil.set(b, a, c);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new AnnotationException(b.getType() + " type is not Context");
        }
    }

    static void bindApp(Object a, Field b, Context c) {
        final App d = b.getAnnotation(App.class);
        if (d == null) {
            return;
        }
        final Context p = c.getApplicationContext();
        final Class<?> f = b.getType();
        if (p.getClass().isAssignableFrom(f)) {
            try {
                AnnotationUtil.set(b, a, p);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            throw new AnnotationException(b.getType() + " type is not Application");
        }
    }

    static void bindSaveInstance(Object a, Field b, Bundle w) {
        if (w == null) {
            return;
        }
        final SaveInstance d = b.getAnnotation(SaveInstance.class);
        if (d == null) {
            return;
        }
        final String h;
        if (d.value().length() > 0) {
            h = d.value();
        } else {
            h = "_" + b.getName();
        }
        final Object i = w.get(h);
        if (i != null) {
            try {
                AnnotationUtil.set(b, a, i);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResString(Object a, Field b, Context c) {
        final ResString d = b.getAnnotation(ResString.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (String.class.isAssignableFrom(g)) {
            try {
                final String f = c.getString(FindResources.string(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResBoolean(Object a, Field b, Context c) {
        final ResBoolean d = b.getAnnotation(ResBoolean.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (boolean.class.isAssignableFrom(g) || Boolean.class.isAssignableFrom(g)) {
            try {
                final boolean f = c.getResources().getBoolean(FindResources.bool(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResDimen(Object a, Field b, Context c) {
        final ResDimen d = b.getAnnotation(ResDimen.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (int.class.isAssignableFrom(g) || Integer.class.isAssignableFrom(g) ||
                float.class.isAssignableFrom(g) || Float.class.isAssignableFrom(g) ||
                double.class.isAssignableFrom(g) || Double.class.isAssignableFrom(g) ||
                long.class.isAssignableFrom(g) || Long.class.isAssignableFrom(g)) {
            try {
                final int f = c.getResources().getDimensionPixelSize(FindResources.dimen(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResInteger(Object a, Field b, Context c) {
        final ResInt d = b.getAnnotation(ResInt.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (int.class.isAssignableFrom(g) || Integer.class.isAssignableFrom(g)) {
            try {
                final float f = c.getResources().getInteger(FindResources.integer(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResStringArray(Object a, Field b, Context c) {
        final ResStringArray d = b.getAnnotation(ResStringArray.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (String[].class.isAssignableFrom(g)) {
            try {
                final String[] f = c.getResources().getStringArray(FindResources.array(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResAnimation(Object a, Field b, Context c) {
        final ResAnimation d = b.getAnnotation(ResAnimation.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (Animation.class.isAssignableFrom(g)) {
            try {
                final Animation f = AnimationUtils.loadAnimation(c, FindResources.anim(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }


    static void bindResLayoutAnimation(Object a, Field b, Context c) {
        final ResLayoutAnimation d = b.getAnnotation(ResLayoutAnimation.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (LayoutAnimationController.class.isAssignableFrom(g)) {
            try {
                final LayoutAnimationController f = AnimationUtils.loadLayoutAnimation(c, FindResources.anim(c, d, b));
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResColor(Object a, Field b, Context c, Resources.Theme t) {
        final ResColor d = b.getAnnotation(ResColor.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (int.class.isAssignableFrom(g) || Integer.class.isAssignableFrom(g)) {
            try {
                final int f;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    f = c.getResources().getColor(FindResources.color(c, d, b), t);
                } else {
                    f = c.getResources().getColor(FindResources.color(c, d, b));
                }
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindResDrawable(Object a, Field b, Context c, Resources.Theme t) {
        final ResDrawable d = b.getAnnotation(ResDrawable.class);
        if (d == null) {
            return;
        }
        final Class<?> g = b.getType();
        if (Drawable.class.isAssignableFrom(g)) {
            try {
                final Drawable f;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    f = c.getResources().getDrawable(FindResources.drwable(c, d, b), t);
                } else {
                    f = c.getResources().getDrawable(FindResources.drwable(c, d, b));
                }
                AnnotationUtil.set(b, a, f);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    static void bindSharedPreferences(Object a, Field b, Context c) {
        final Pref d = b.getAnnotation(Pref.class);
        if (d == null) {
            return;
        }
        final Class<?> f = b.getType();
        final Object i = SharedPrefHelp.onCreate(c, f);
        try {
            AnnotationUtil.set(b, a, i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void bindHandler(Object a, Field b, Context c) {
        final HandlerService d = b.getAnnotation(HandlerService.class);
        if (d == null) {
            return;
        }
        final Class<?> f = b.getType();
        final ThreadHandler g = f.getAnnotation(ThreadHandler.class);
        if (g == null) {
            return;
        }
        final Object i = Proxy.newProxyInstance(f.getClassLoader(), new Class<?>[]{f}, new HandlerInvocationHandler(a));
        try {
            AnnotationUtil.set(b, a, i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void bindSystemService(Object a, Field b, Context c) {
        final SystemService d = b.getAnnotation(SystemService.class);
        if (d == null) {
            return;
        }
        final Class<?> f = b.getType();
        Object i = null;
        if (WindowManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.WINDOW_SERVICE);
        } else if (LayoutInflater.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } else if (ActivityManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.ACTIVITY_SERVICE);
        } else if (PowerManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.POWER_SERVICE);
        } else if (AlarmManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.ALARM_SERVICE);
        } else if (NotificationManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.NOTIFICATION_SERVICE);
        } else if (KeyguardManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.KEYGUARD_SERVICE);
        } else if (LocationManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.LOCATION_SERVICE);
        } else if (SearchManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.SEARCH_SERVICE);
        } else if (SensorManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.SENSOR_SERVICE);
        } else if (StorageManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.STORAGE_SERVICE);
        } else if (Vibrator.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.VIBRATOR_SERVICE);
        } else if (ConnectivityManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.CONNECTIVITY_SERVICE);
        } else if (WifiManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.WIFI_SERVICE);
        } else if (AudioManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.AUDIO_SERVICE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && MediaRouter.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.MEDIA_ROUTER_SERVICE);
        } else if (TelephonyManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.TELEPHONY_SERVICE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && SubscriptionManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && CarrierConfigManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.CARRIER_CONFIG_SERVICE);
        } else if (InputMethodManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.INPUT_METHOD_SERVICE);
        } else if (UiModeManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.UI_MODE_SERVICE);
        } else if (DownloadManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.DOWNLOAD_SERVICE);
        } else if (BatteryManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.BATTERY_SERVICE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && JobScheduler.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && NetworkStatsManager.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.NETWORK_STATS_SERVICE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && JobScheduler.class.isAssignableFrom(f)) {
            i = c.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }
        try {
            AnnotationUtil.set(b, a, i);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

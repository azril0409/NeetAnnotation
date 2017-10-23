package library.neetoffice.com.neetannotation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Deo-chainmeans on 2017/5/20.
 */

public class SharedPrefInvocationHandler implements InvocationHandler {
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public SharedPrefInvocationHandler(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        final Type type = method.getReturnType();
        if (type == Void.TYPE) {
            if (args.length == 0) {
                final PrefRemove perRemove = method.getAnnotation(PrefRemove.class);
                if (perRemove != null) {
                    int[] keyRes = perRemove.keyRes();
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    for (int keyRe : keyRes) {
                        if (keyRe != -1) {
                            edit = edit.remove(context.getString(keyRe));
                        }
                        edit.apply();
                    }
                }
                return null;
            } else if (args.length == 1) {
                final Object arg = args[0];
                if (arg == null) {
                    return null;
                }
                if (arg instanceof Boolean) {
                    putBoolean(method, (Boolean) arg);
                    return null;
                }
                if (arg instanceof Float) {
                    putFloat(method, (Float) arg);
                    return null;
                }
                if (arg instanceof Integer) {
                    putInt(method, (Integer) arg);
                    return null;
                }
                if (arg instanceof Long) {
                    putLong(method, (Long) arg);
                    return null;
                }
                if (arg instanceof String) {
                    putString(method, (String) arg);
                    return null;
                }
                if (arg instanceof Collection) {
                    final HashSet<String> set = new HashSet<>();
                    final Collection l = (Collection) arg;
                    for (Object o : l) {
                        set.add(o.toString());
                    }
                    putStringSet(method, set);
                    return null;
                }
            } else if (args.length > 1) {
                final HashSet<String> set = new HashSet<>();
                for (Object arg : args) {
                    if (arg instanceof Collection) {
                        final Collection l = (Collection) arg;
                        for (Object o : l) {
                            set.add(o.toString());
                        }
                    } else {
                        set.add(arg.toString());
                    }
                }
                putStringSet(method, set);
                return null;
            }
        } else {
            if (type == Boolean.TYPE) {
                return getBoolean(method);
            } else if (type == Float.TYPE) {
                return getFloat(method);
            } else if (type == Integer.TYPE) {
                return getInt(method);
            } else if (type == Long.TYPE) {
                return getLong(method);
            } else if (type == String.class) {
                return getString(method);
            } else if (type == Collection.class) {
                return getHashSet(method);
            } else if (type == List.class) {
                return getArrayList(method);
            } else if (type == ArrayList.class) {
                return getArrayList(method);
            } else if (type == Set.class) {
                return getHashSet(method);
            } else if (type == HashSet.class) {
                return getHashSet(method);
            }
        }
        return null;
    }

    private String getPerKey(final Method method) {
        final PrefKey perKey = method.getAnnotation(PrefKey.class);
        if (perKey != null && !perKey.value().isEmpty()) {
            return perKey.value();
        } else if (perKey != null && perKey.keyRes() != -1) {
            return context.getString(perKey.keyRes());
        }
        return method.getName();
    }

    private void putBoolean(final Method method, final Boolean arg) {
        final String key = getPerKey(method);
        sharedPreferences.edit().putBoolean(key, arg).apply();
    }

    private void putFloat(Method method, Float arg) {
        final String key = getPerKey(method);
        sharedPreferences.edit().putFloat(key, arg).apply();
    }

    private void putInt(Method method, Integer arg) {
        final String key = getPerKey(method);
        sharedPreferences.edit().putInt(key, arg).apply();
    }

    private void putLong(Method method, Long arg) {
        final String key = getPerKey(method);
        sharedPreferences.edit().putLong(key, arg).apply();
    }

    private void putString(Method method, String arg) {
        final String key = getPerKey(method);
        sharedPreferences.edit().putString(key, arg).apply();
    }

    private void putStringSet(Method method, Set<String> arg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final String key = getPerKey(method);
            sharedPreferences.edit().putStringSet(key, arg).apply();
        }
    }

    private Object getBoolean(final Method method) {
        final DefaultBoolean defaultBoolean = method.getAnnotation(DefaultBoolean.class);
        final String key = getPerKey(method);
        final boolean defValue;
        if (defaultBoolean != null) {
            defValue = defaultBoolean.value();
        } else {
            defValue = false;
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    private Object getFloat(Method method) {
        final DefaultFloat defaultFloat = method.getAnnotation(DefaultFloat.class);
        final String key = getPerKey(method);
        final float defValue;
        if (defaultFloat != null) {
            defValue = defaultFloat.value();
        } else {
            defValue = 0.0F;
        }
        return sharedPreferences.getFloat(key, defValue);
    }

    private Object getInt(Method method) {
        final DefaultInt defaultInt = method.getAnnotation(DefaultInt.class);
        final String key = getPerKey(method);
        final int defValue;
        if (defaultInt != null) {
            defValue = defaultInt.value();
        } else {
            defValue = 0;
        }
        return sharedPreferences.getInt(key, defValue);
    }

    private Object getLong(Method method) {
        final DefaultLong defaultLong = method.getAnnotation(DefaultLong.class);
        final String key = getPerKey(method);
        final long defValue;
        if (defaultLong != null) {
            defValue = defaultLong.value();
        } else {
            defValue = 0L;
        }
        return sharedPreferences.getLong(key, defValue);
    }

    private Object getString(Method method) {
        final DefaultString defaultString = method.getAnnotation(DefaultString.class);
        final String key = getPerKey(method);
        final String defValue;
        if (defaultString != null) {
            defValue = defaultString.value();
        } else {
            defValue = "";
        }
        return sharedPreferences.getString(key, defValue);
    }

    private Object getHashSet(Method method) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final String key = getPerKey(method);
            return sharedPreferences.getStringSet(key, new HashSet<String>());
        }
        return new HashSet<String>();
    }

    private Object getArrayList(Method method) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final String key = getPerKey(method);
            return new ArrayList<>(sharedPreferences.getStringSet(key, new HashSet<String>()));
        }
        return new ArrayList<String>();
    }
}

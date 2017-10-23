package library.neetoffice.com.neetannotation;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Deo-chainmeans on 2016/8/27.
 */
abstract class BindBundle {
    static void addFieldToBundle(Object a, Field g, String h, Bundle b) {
        g.setAccessible(true);
        try {
            final Object i = g.get(a);
            if (g.getType() == byte.class) {
                b.putByte(h, (byte) i);
            } else if (g.getType() == Byte.class) {
                b.putByte(h, (Byte) i);
            } else if (g.getType() == byte[].class) {
                b.putByteArray(h, (byte[]) i);
            } else if (g.getType() == boolean.class) {
                b.putBoolean(h, (boolean) i);
            } else if (g.getType() == Boolean.class) {
                b.putBoolean(h, (Boolean) i);
            } else if (g.getType() == boolean[].class) {
                b.putBooleanArray(h, (boolean[]) i);
            } else if (g.getType() == short.class) {
                b.putShort(h, (short) i);
            } else if (g.getType() == Short.class) {
                b.putShort(h, (Short) i);
            } else if (g.getType() == short[].class) {
                b.putShortArray(h, (short[]) i);
            } else if (g.getType() == int.class) {
                b.putInt(h, (int) i);
            } else if (g.getType() == Integer.class) {
                b.putInt(h, (Integer) i);
            } else if (g.getType() == int[].class) {
                b.putIntArray(h, (int[]) i);
            } else if (g.getType() == long.class) {
                b.putLong(h, (long) i);
            } else if (g.getType() == Long.class) {
                b.putLong(h, (Long) i);
            } else if (g.getType() == long[].class) {
                b.putLongArray(h, (long[]) i);
            } else if (g.getType() == float.class) {
                b.putFloat(h, (float) i);
            } else if (g.getType() == Float.class) {
                b.putFloat(h, (Float) i);
            } else if (g.getType() == float[].class) {
                b.putFloatArray(h, (float[]) i);
            } else if (g.getType() == double.class) {
                b.putDouble(h, (double) i);
            } else if (g.getType() == Double.class) {
                b.putDouble(h, (Double) i);
            } else if (g.getType() == double[].class) {
                b.putDoubleArray(h, (double[]) i);
            } else if (g.getType() == char.class) {
                b.putChar(h, (char) i);
            } else if (g.getType() == Character.class) {
                b.putChar(h, (Character) i);
            } else if (g.getType() == char[].class) {
                b.putCharArray(h, (char[]) i);
            } else if (g.getType() == String.class) {
                b.putString(h, (String) i);
            } else if (g.getType() == String[].class) {
                b.putStringArray(h, (String[]) i);
            } else if (g.getType() == CharSequence.class) {
                b.putCharSequence(h, (CharSequence) i);
            } else if (g.getType() == CharSequence[].class) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    b.putCharSequenceArray(h, (CharSequence[]) i);
                }
            } else if (Parcelable.class.isAssignableFrom(g.getType())) {
                b.putParcelable(h, (Parcelable) i);
            } else if (Parcelable[].class.isAssignableFrom(g.getType())) {
                b.putParcelableArray(h, (Parcelable[]) i);
            } else if (g.getType() == ArrayList.class) {
                b.putSerializable(h, (ArrayList<?>) i);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        g.setAccessible(false);
    }
}

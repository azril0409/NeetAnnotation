package library.neetoffice.com.neetannotation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Deo on 2016/4/6.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface NReceiver {
}
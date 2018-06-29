package library.neetoffice.com.neetannotation.viewmodesupport;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Publisher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import library.neetoffice.com.neetannotation.BindBase;

/**
 * Created by azril on 2018/2/21.
 */

public class BindViewModel {
    public static void bindViewModelProvider(Object a, Field b, Context c){
        final ViewModelProvider d = b.getAnnotation(ViewModelProvider.class);
        if (d == null){
            return;
        }
        final Class e = d.getClass();
        final Field[] f = e.getDeclaredFields();
        for (Field g:f){
            BindBase.baseFieldBind(a , g , c);
        }
        if ( a instanceof FragmentActivity) {
            ViewModelProviders.of((FragmentActivity)a);
        }else if ( a instanceof android.support.v4.app.Fragment) {
            ViewModelProviders.of((android.support.v4.app.Fragment)a);
        }
    }


    public static void bindPublishSubject(Object a, Field b) {
        if (PublishSubject.class.isAssignableFrom(b.getType())) {
            try {
                final Object c = b.get(a);
                if (c == null) {
                    b.set(a, PublishSubject.create());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void bindObserve(Object a, Method b, Context c) {
        final Observe d = b.getAnnotation(Observe.class);
        if (d == null) {
            return;
        }
        final String e = d.viewmodelName();
        final Class f = a.getClass();
        try {
            final Field g = f.getDeclaredField(e);
            final ViewModel h = (ViewModel) g.get(a);
            final Class i = g.getType();
            final String j = getSubscriberName(b, d);
            final Field k = i.getDeclaredField(j);
            if (Publisher.class.isAssignableFrom(k.getType())) {
                final PublishSubject s = (PublishSubject) k.get(h);
                s.subscribe(new NeetConsumer(a, b));
            }
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }

    public static String getSubscriberName(Method a, Observe b) {
        final String subscriber = b.subscriber();
        if (subscriber.isEmpty()) {
            return a.getName();
        }
        return subscriber;
    }
}

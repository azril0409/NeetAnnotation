package library.neetoffice.com.neetannotation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Deo-chainmeans on 2017/4/29.
 */

public abstract class AnnotationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AnnotationHelp.onReceive(this, context, intent);
    }
}

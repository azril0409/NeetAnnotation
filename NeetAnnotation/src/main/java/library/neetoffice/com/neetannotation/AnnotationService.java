package library.neetoffice.com.neetannotation;

import android.app.Service;
import android.content.Intent;

/**
 * Created by Deo-chainmeans on 2017/9/8.
 */
@NService
public abstract class AnnotationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        BindService.onCreate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BindService.onStartCommand(this, intent);
        final NService f = getClass().getAnnotation(NService.class);
        if (f != null) {
            return f.value();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

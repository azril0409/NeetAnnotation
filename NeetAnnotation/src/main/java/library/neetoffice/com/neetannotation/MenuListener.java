package library.neetoffice.com.neetannotation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.MenuItem;
import android.widget.Toolbar;


/**
 * Created by Deo-chainmeans on 2017/5/2.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class MenuListener implements Toolbar.OnMenuItemClickListener {
    final Activity a;

    MenuListener(Activity a) {
        this.a = a;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return a.onOptionsItemSelected(item);
    }
}

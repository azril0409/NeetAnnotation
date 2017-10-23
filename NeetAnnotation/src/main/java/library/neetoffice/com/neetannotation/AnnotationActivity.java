package library.neetoffice.com.neetannotation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Deo-chainmeans on 2017/4/29.
 */
@NActivity
public abstract class AnnotationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotationHelp.onCreate(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AnnotationHelp.onSaveInstanceState(this, outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AnnotationHelp.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return AnnotationHelp.onCreateOptionsMenu(this, getMenuInflater(), menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final boolean b = super.onOptionsItemSelected(item);
        return b | AnnotationHelp.onOptionsItemSelected(this, item);
    }
}

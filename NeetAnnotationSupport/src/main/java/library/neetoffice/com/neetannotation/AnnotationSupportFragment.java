package library.neetoffice.com.neetannotation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import library.neetoffice.com.neetannotation.AnnotationHelp;

/**
 * Created by Deo-chainmeans on 2017/10/23.
 */

public class AnnotationSupportFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnnotationHelp.onCreate(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return AnnotationHelp.onCreateView(this, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AnnotationHelp.onSaveInstanceState(this, outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AnnotationHelp.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        AnnotationHelp.onCreateOptionsMenu(this, inflater, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final boolean b = super.onOptionsItemSelected(item);
        return b | AnnotationHelp.onOptionsItemSelected(this, item);
    }
}

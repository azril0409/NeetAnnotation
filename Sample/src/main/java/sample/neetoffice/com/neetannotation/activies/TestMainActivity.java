package sample.neetoffice.com.neetannotation.activies;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import javax.inject.Inject;
import javax.inject.Named;

import library.neetoffice.com.neetannotation.AfterAnnotation;
import library.neetoffice.com.neetannotation.Click;
import library.neetoffice.com.neetannotation.ItemClick;
import library.neetoffice.com.neetannotation.NActivity;
import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.Subscribe;
import library.neetoffice.com.neetannotation.TextChange;
import library.neetoffice.com.neetannotation.Touch;
import library.neetoffice.com.neetannotation.TouchUp;
import library.neetoffice.com.neetannotation.UIThread;
import library.neetoffice.com.neetannotation.ViewById;
import library.neetoffice.com.neetannotation.ViewModelOf;
import sample.neetoffice.com.neetannotation.MonyModule;
import sample.neetoffice.com.neetannotation.R;
import sample.neetoffice.com.neetannotation.User;
import sample.neetoffice.com.neetannotation.UserModel;
import sample.neetoffice.com.neetannotation.UserModule;

@NActivity(value = R.layout.activity_main)
@NDagger(modules = {UserModule.class, MonyModule.class})
public class TestMainActivity extends AppCompatActivity {
    @ViewById
    TextView text;
    @Inject
    @Named("deo")
    User deo;
    @Subscribe(UserModel.class)
    @Named("deo")
    User user;
    @ViewModelOf
    UserModel userModel;

    @AfterAnnotation
    void onAfterAnnotation(Bundle bundle) {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe(UserModel.class)
    @Named("deo")
    @UIThread
    public void accept(User user) {

    }

    @Click
    void ontextClicked(Button view) {
    }

    @TextChange(R.id.text)
    void textTextChange(View view, String text) {

    }

    @TouchUp
    boolean textTouchUped(View view) {
        return false;
    }
}

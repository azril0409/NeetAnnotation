package sample.neetoffice.com.neetannotation;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import javax.inject.Named;

import library.neetoffice.com.neetannotation.NDagger;
import library.neetoffice.com.neetannotation.NViewModel;
import library.neetoffice.com.neetannotation.Subject;

@NViewModel
@NDagger(modules = UserModule.class)
public class UserModel extends AndroidViewModel {
    @Subject
    @Named("deo")
    UserInteractor deo;

    @Subject
    @Named("tony")
    UserInteractor tony;

    public UserModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

package sample.neetoffice.com.neetannotation;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Named("deo")
    @Provides
    public User deo() {
        return new User();
    }

    @Named("tony")
    @Provides
    public User tony() {
        return new User();
    }
}

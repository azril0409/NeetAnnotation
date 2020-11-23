package sample.neetoffice.com.neetannotation

import android.app.Application
import library.neetoffice.com.neetannotation.AfterInject
import library.neetoffice.com.neetannotation.NApplication

@NApplication
open class App : Application() {
    @AfterInject
    fun onInjected() {
    }
}
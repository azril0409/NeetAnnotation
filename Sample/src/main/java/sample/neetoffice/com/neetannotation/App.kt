package sample.neetoffice.com.neetannotation

import android.app.Application
import library.neetoffice.com.neetannotation.AfterInject
import library.neetoffice.com.neetannotation.NApplication
import library.neetoffice.com.neetdao.Dao
import sample.neetoffice.com.neetannotation.models.Record
import javax.inject.Inject

@NApplication
open class App : Application() {
    @Inject
    protected lateinit var record: Record
    @AfterInject
    fun onInjected() {
    }
}
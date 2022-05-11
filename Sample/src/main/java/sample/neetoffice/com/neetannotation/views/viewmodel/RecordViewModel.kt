package sample.neetoffice.com.neetannotation.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.models.RecordInteractor
import sample.neetoffice.com.neetannotation.models.RecordListinteractor
import sample.neetoffice.com.neetannotation.modules.RecordModule
import text.sss.support.IntegerInteractor
import text.sss.support.ThrowableInteractor

@NViewModel(isSingle = true)
@NDagger(modules = [RecordModule::class])
open class RecordViewModel(application: Application) : AndroidViewModel(application) {

    @Published
    lateinit var onRecord: RecordInteractor

    @Published
    lateinit var onRecords: RecordListinteractor

    @Published
    lateinit var value: IntegerInteractor

    @Published(recordLastEntity = false)
    @NamedAs("onError")
    lateinit var error: ThrowableInteractor


    @AfterInject
    fun onAfterAnnotation() {
    }

    @OnCreate
    @OnStart
    @OnResume
    @OnPause
    @OnStop
    @OnDestroy
    @ThreadOn(ThreadOn.Mode.UIThread)
    open fun a() {
    }

    @OnStop
    fun b() {
    }

    fun load() = arrayListOf<Record>()
    fun insert(record: Record) {}
    fun delete(record: Record) {}
}
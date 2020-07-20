package sample.neetoffice.com.neetannotation.views.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import library.neetoffice.com.neetannotation.*
import library.neetoffice.com.neetdao.Dao
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.models.RecordInteractor
import sample.neetoffice.com.neetannotation.models.RecordListinteractor
import sample.neetoffice.com.neetannotation.modules.RecordModule
import text.sss.support.IntegerInteractor
import text.sss.support.ThrowableInteractor
import javax.inject.Inject

@NViewModel(isSingle = true)
@NDagger(modules = [RecordModule::class])
open class RecordViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var dao: Dao<Record>
    @Published
    @InjectInitialEntity
    lateinit var onRecord: RecordInteractor
    @Published
    lateinit var onRecords: RecordListinteractor
    @Published
    lateinit var value: IntegerInteractor
    @Published
    lateinit var onError: ThrowableInteractor

    @AfterInject
    fun onAfterAnnotation(){
    }

    fun load() = dao.list()
    fun insert(record: Record) = dao.insert(record)
    fun delete(record: Record) = dao.delete(record)
}
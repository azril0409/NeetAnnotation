package sample.neetoffice.com.neetannotation.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import library.neetoffice.com.neetannotation.*
import library.neetoffice.com.neetdao.Dao
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.models.RecordInteractor
import sample.neetoffice.com.neetannotation.modules.RecordModule
import javax.inject.Inject
import javax.inject.Named

@NViewModel
@NDagger(modules = [RecordModule::class])
open class RecordViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var dao: Dao<Record>
    @Subject(name = "A")
    @InjectEntity
    lateinit var recordPresenter: RecordInteractor

    @AfterAnnotation
    fun onAfterAnnotation(){

    }

    fun load() = dao.list()
    fun insert(record: Record) = dao.insert(record)
    fun delete(record: Record) = dao.delete(record)
}
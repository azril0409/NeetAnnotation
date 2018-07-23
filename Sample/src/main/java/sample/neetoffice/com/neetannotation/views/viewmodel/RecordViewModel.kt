package sample.neetoffice.com.neetannotation.views.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.util.Log
import library.neetoffice.com.neetannotation.NDagger
import library.neetoffice.com.neetannotation.NViewModel
import library.neetoffice.com.neetdao.Dao
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.modules.RecordModule
import javax.inject.Inject

@NViewModel
@NDagger(modules = arrayOf(RecordModule::class))
open class RecordViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var dao: Dao<Record>

    fun load() = dao.list()
    fun insert(record: Record) = dao.insert(record)
    fun delete(record: Record) = dao.delete(record)
}
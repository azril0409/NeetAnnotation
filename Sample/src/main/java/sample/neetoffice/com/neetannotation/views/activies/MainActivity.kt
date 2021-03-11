package sample.neetoffice.com.neetannotation.views.activies

import android.app.Application
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.R
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.modules.RecordModule
import sample.neetoffice.com.neetannotation.views.adapters.RecordAdapter
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@NActivity(R.layout.activity_main)
@OptionsMenu(R.menu.mian)
open class MainActivity : AppCompatActivity() {
    @ViewById(R.id.listView)
    lateinit var listView: ListView
    @Inject
    lateinit var adapter: RecordAdapter
    @ViewModelOf
    lateinit var recordViewModel: RecordViewModel

    @AfterInject
    fun onAfter() {
        Log.d("TAG","$recordViewModel")
        Log.d("TAG","${recordViewModel.getApplication<Application>()}")
        listView.adapter = adapter
        var toolbar: Toolbar

        // adapter.setAll(viewModel.load())
    }

    @Click(R.id.action)
    open fun onActionClicked() {
        var record = Record(Calendar.getInstance().time)
        //record.id = viewModel.insert(record).toInt()
        adapter.add(record)
    }

    @ItemLongClick(R.id.listView)
    fun deleteItem(record: Record) {
        // viewModel.delete(record)
        adapter.remove(record)
    }

    @FocusChange(R.id.listView)
    fun onFocusChange(hasFocus: Boolean) {
    }

    @Subscribes(Subscribe(viewmode = RecordViewModel::class))
    @NamedAs("onRecord")
    fun onRecord2(record: Record, error:Throwable) {

    }

    @Subscribe(viewmode = RecordViewModel::class)
    @NamedAs("onError")
    fun onError() {
    }

    @ActivityResult(200)
    fun onResult(@ActivityResult.Extra("aaaa") name: String,@ActivityResult.Extra("aaaa") secndNane: String) {

    }
}
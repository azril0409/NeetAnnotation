package sample.neetoffice.com.neetannotation.views.activies

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.databinding.ActivityMainBinding
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.views.activies.MainActivity_.binding
import sample.neetoffice.com.neetannotation.views.adapters.RecordAdapter
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel
import java.util.*
import javax.inject.Inject

@NActivity(ActivityMainBinding::class)
@OptionsMenu("mian")
open class MainActivity : AppCompatActivity() {

    @ViewById
    lateinit var listView: ListView

    @Inject
    lateinit var adapter: RecordAdapter

    @ViewModelOf
    lateinit var recordViewModel: RecordViewModel

    @AfterInject
    fun onAfter() {
        setSupportActionBar(binding.toolbar)
        listView.adapter = adapter
        // adapter.setAll(viewModel.load())
    }

    @OnStart
    @OnCreate
    fun start() {
    }

    @OnResume
    fun rsume() {
    }

    @OnPause
    fun pause() {
    }

    @OnStop
    fun stop() {
    }

    @OnDestroy
    fun destroy(){}

    @Click()
    open fun onActionClicked() {
        var record = Record(Calendar.getInstance().time)
        //record.id = viewModel.insert(record).toInt()
        adapter.add(record)
    }

    @ItemLongClick("listView")
    fun deleteItem(record: Record) {
        // viewModel.delete(record)
        adapter.remove(record)
    }

    @FocusChange("listView")
    fun onFocusChange(hasFocus: Boolean) {
    }

    @Subscribes(Subscribe(viewmode = RecordViewModel::class))
    @NamedAs("onRecord")
    fun onRecord2(record: Record, error: Throwable) {

    }

    @Subscribe(viewmode = RecordViewModel::class)
    @NamedAs("onError")
    fun onError() {
    }

    @ActivityResult(200)
    fun onResult(
        @ActivityResult.Extra("aaaa") name: String,
        @ActivityResult.Extra("aaaa") secndNane: String
    ) {

    }
}
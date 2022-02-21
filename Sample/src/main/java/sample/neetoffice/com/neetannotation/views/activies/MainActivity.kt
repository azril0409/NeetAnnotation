package sample.neetoffice.com.neetannotation.views.activies

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.databinding.ActivityMainBinding
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.views.adapters.RecordAdapter
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel
import java.util.*
import javax.inject.Inject
import kotlin.contracts.contract

@NActivity(ActivityMainBinding::class)
@OptionsMenu("mian")
open class MainActivity : AppCompatActivity() {
    @ViewById
    protected lateinit var toolbar: Toolbar

    @ViewById
    @InjectInitialEntity
    lateinit var listView: ListView

    @Inject
    lateinit var adapter: RecordAdapter

    @ViewModelOf
    lateinit var recordViewModel: RecordViewModel

    @ResColor("background_dark", resPackage = "android")
    @JvmField
    protected var color: Int = Color.BLACK

    @AfterInject
    fun onAfter() {
        android.Manifest.permission.BLUETOOTH
        setSupportActionBar(toolbar)
        //listView.adapter = adapter
        // adapter.setAll(viewModel.load())
    }

    @OnStart
    @OnCreate
    fun start() {
    }

    @OnResume
    fun resume() {
    }

    @OnPause
    fun pause() {
    }

    @OnStop
    fun stop() {
    }

    @OnDestroy
    fun destroy() {
    }

    @Click()
    open fun onActionClicked() {
        var record = Record(Calendar.getInstance().time)
        //record.id = viewModel.insert(record).toInt()
        adapter.add(record)
    }

    @ItemClick("listView")
    fun itemClicked(record: Record) {
        Test_.IntentBuilder(this).startActivity()
    }

    @ItemLongClick("listView")
    fun deleteItem(record: Record) {
        // viewModel.delete(record)
        //adapter.remove(record)
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

    @ActivityResult(ActivityResult.Contract.OpenMultipleDocuments)
    fun onResult(uris: List<Uri>) {
    }

    @ActivityResult(
        ActivityResult.Contract.OpenMultipleDocuments,
        resultCode = RESULT_CANCELED
    )
    fun onResult(
        @ActivityResult.Extra("aaaa") name: String
    ) {
    }

    @ActivityResult()
    fun onResult(bundle: Bundle) {
    }
}

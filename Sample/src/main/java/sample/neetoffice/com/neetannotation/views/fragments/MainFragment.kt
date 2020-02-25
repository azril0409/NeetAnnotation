package sample.neetoffice.com.neetannotation.views.fragments

import android.os.Bundle
import android.widget.ListView
import androidx.fragment.app.Fragment
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.R
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel
import javax.inject.Named

@NFragment(R.layout.activity_main)
open class MainFragment: Fragment() {
    @ViewById(R.id.listView)
    lateinit var listView: ListView
    @Extra
    lateinit var list:ArrayList<Record>


    @Subscribes(Subscribe(viewmode = RecordViewModel::class,key = "test"))
    @Named("onRecord")
    fun onRecord2(record: Record) {

    }
}
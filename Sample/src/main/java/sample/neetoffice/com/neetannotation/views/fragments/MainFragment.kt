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
@OptionsMenu(R.menu.mian)
open class MainFragment : Fragment() {
    @ViewById(R.id.listView)
    lateinit var listView: ListView

    @Extra
    lateinit var list: ArrayList<Record>


    @Subscribes(Subscribe(viewmode = RecordViewModel::class))
    @Named("onRecord")
    fun onRecord2(record: Record) {

    }

    @Subscribe(viewmode = RecordViewModel::class)
    fun onRecords(list: List<Record>) {

    }

    @ActivityResult(20)
    fun onActivityResult(test: String) {
    }
}
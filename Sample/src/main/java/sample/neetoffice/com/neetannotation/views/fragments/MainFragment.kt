package sample.neetoffice.com.neetannotation.views.fragments

import android.os.Bundle
import android.widget.ListView
import androidx.fragment.app.Fragment
import library.neetoffice.com.neetannotation.Extra
import library.neetoffice.com.neetannotation.NFragment
import library.neetoffice.com.neetannotation.ViewById
import sample.neetoffice.com.neetannotation.R
import sample.neetoffice.com.neetannotation.models.Record

@NFragment(R.layout.activity_main)
open class MainFragment: Fragment() {
    @ViewById(R.id.listView)
    lateinit var listView: ListView
    @Extra
    lateinit var list:ArrayList<Record>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
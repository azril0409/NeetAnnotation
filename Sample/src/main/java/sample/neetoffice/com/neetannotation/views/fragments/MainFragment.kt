package sample.neetoffice.com.neetannotation.views.fragments

import android.widget.ListView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.R
import sample.neetoffice.com.neetannotation.databinding.ActivityMainBinding
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel
import javax.inject.Named

@NFragment(ActivityMainBinding::class)
@OptionsMenu("mian")
open class MainFragment : Fragment() {
    @ViewById
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

    @ActivityResult()
    fun onActivityResult(@ActivityResult.Extra test: String) {
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
    fun destroy() {
    }
}
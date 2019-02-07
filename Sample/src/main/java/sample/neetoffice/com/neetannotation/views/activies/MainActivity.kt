package sample.neetoffice.com.neetannotation.views.activies

import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.R
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.modules.RecordModule
import sample.neetoffice.com.neetannotation.views.adapters.RecordAdapter
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel
import java.util.*
import javax.inject.Inject

@NActivity(R.layout.activity_main)
@NDagger(modules = arrayOf(RecordModule::class))
open class MainActivity : AppCompatActivity() {
    @ViewById(R.id.listView)
    lateinit var listView: ListView
    @ViewModelOf
    lateinit var viewModel: RecordViewModel
    @Inject
    lateinit var adapter: RecordAdapter
    @Extra
    lateinit var list:ArrayList<Record>

    @AfterAnnotation
    fun onAfter() {
        listView.adapter = adapter
        var toolbar:Toolbar
        adapter.setAll(viewModel.load())
    }

    @Click(R.id.action)
    @ThreadOn(ThreadOn.Mode.UIThread)
    open fun onActionClicked() {
        var record = Record(Calendar.getInstance().time)
        record.id = viewModel.insert(record).toInt()
        adapter.add(record)
    }

    @ItemLongClick(R.id.listView)
    fun deleteItem(record: Record) {
        viewModel.delete(record)
        adapter.remove(record)
    }
}
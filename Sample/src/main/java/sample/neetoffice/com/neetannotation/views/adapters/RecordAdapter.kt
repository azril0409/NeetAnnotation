package sample.neetoffice.com.neetannotation.views.adapters

import android.content.Context
import android.view.View
import android.widget.TextView
import library.neetoffice.com.genericadapter.base.GenericAdapter
import sample.neetoffice.com.neetannotation.models.Record

class Tag {
    lateinit var text: TextView
}

class RecordAdapter(context: Context) : GenericAdapter<Record, Tag>(context, android.R.layout.simple_list_item_1) {

    init {
        setSort({ r1: Record, r2: Record ->
            (r2.time - r1.time).toInt()
        })
    }

    override fun onCreateTag(view: View): Tag {
        var tag = Tag()
        tag.text = view.findViewById(android.R.id.text1)
        return tag
    }

    override fun onBind(tag: Tag, record: Record) {
        tag.text.text = "${record.id ?: ""}. ${record.timeString}"
    }
}
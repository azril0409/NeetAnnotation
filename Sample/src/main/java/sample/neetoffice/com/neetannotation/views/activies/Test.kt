package sample.neetoffice.com.neetannotation.views.activies

import androidx.appcompat.app.AppCompatActivity
import library.neetoffice.com.neetannotation.*
import sample.neetoffice.com.neetannotation.models.Record

@NActivity
open class Test : AppCompatActivity() {
    @Extra
    lateinit var a: Record

    @Extra
    lateinit var b: Array<Record>

    @ItemClick
    @ItemLongClick
    @ItemSelect
    fun itemClick(s: String) {
    }
}
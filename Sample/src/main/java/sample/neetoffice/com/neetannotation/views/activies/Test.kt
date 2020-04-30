package sample.neetoffice.com.neetannotation.views.activies

import androidx.appcompat.app.AppCompatActivity
import library.neetoffice.com.neetannotation.Extra
import library.neetoffice.com.neetannotation.NActivity
import sample.neetoffice.com.neetannotation.models.Record

@NActivity
open class Test:AppCompatActivity (){
    @Extra
    lateinit var a:Record
}
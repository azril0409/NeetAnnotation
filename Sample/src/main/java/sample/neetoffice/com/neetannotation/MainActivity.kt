package sample.neetoffice.com.neetannotation

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import library.neetoffice.com.neetannotation.*

@NActivity(R.layout.activity_main)
open class MainActivity : AppCompatActivity() {
    @ViewById(R.id.text)
    lateinit var text: TextView
    @ResString
    lateinit var app_name: String
    @ResColor
    @JvmField
    var colorPrimary: Int = 0

    @AfterAnnotation
    fun onAfterAnnotation() {

    }

    @Touch(R.id.text)
    open fun onTouched(a: Int, b: Int, text: View): Boolean {
        return false
    }
}

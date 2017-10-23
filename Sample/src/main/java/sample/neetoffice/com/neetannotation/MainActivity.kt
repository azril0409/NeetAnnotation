package sample.neetoffice.com.neetannotation

import android.widget.TextView
import library.neetoffice.com.neetannotation.AfterAnnotation
import library.neetoffice.com.neetannotation.AnnotationCompatActivity
import library.neetoffice.com.neetannotation.NActivity
import library.neetoffice.com.neetannotation.ViewById

@NActivity(R.layout.activity_main)
class MainActivity : AnnotationCompatActivity() {
    @ViewById
    lateinit var text: TextView

    @AfterAnnotation
    fun init() {
        text.text = "Hello Android!"
    }
}

package sample.neetoffice.com.neetannotation.views.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import library.neetoffice.com.neetannotation.AfterInject
import library.neetoffice.com.neetannotation.NView
import library.neetoffice.com.neetannotation.ViewById
import sample.neetoffice.com.neetannotation.databinding.ViewSimpleBinding

@NView(ViewSimpleBinding::class)
open class SimpleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    @ViewById
    lateinit var textView: TextView

    @AfterInject
    protected fun onInjected(){
        textView.text = "This is a test view"
    }
}
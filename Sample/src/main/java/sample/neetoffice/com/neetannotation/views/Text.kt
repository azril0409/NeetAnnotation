package sample.neetoffice.com.neetannotation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import library.neetoffice.com.neetannotation.NView
import library.neetoffice.com.neetannotation.ViewModelOf
import sample.neetoffice.com.neetannotation.views.viewmodel.RecordViewModel

@NView
open class Text @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    @ViewModelOf
    lateinit var recordViewModel: RecordViewModel
}
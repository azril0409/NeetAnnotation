package sample.neetoffice.com.neetannotation

import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import library.neetoffice.com.neetannotation.*

@NActivity(R.layout.activity_main)
@OptionsMenu(R.menu.mian)
class MainActivity : AnnotationCompatActivity() {
    @RestService
    lateinit var api: Api

    @Click
    fun button1Clicked(view: TextView) {
        Toast.makeText(this, view.text.toString(), Toast.LENGTH_SHORT).show()
    }

    @Click
    fun button2(view: TextView) {
        Toast.makeText(this, view.text.toString(), Toast.LENGTH_SHORT).show()
    }

    @Click(R.id.button3)
    fun onButton3Clicked(view: TextView) {
        Toast.makeText(this, view.text.toString(), Toast.LENGTH_SHORT).show()
    }

    @OptionsItem
    fun menu1Selected(menu: MenuItem) {
        Toast.makeText(this, menu.title, Toast.LENGTH_SHORT).show()
    }

    @OptionsItem
    fun menu2(menu: MenuItem) {
        Toast.makeText(this, menu.title, Toast.LENGTH_SHORT).show()
    }

    @OptionsItem(R.id.menu3)
    fun onMenu3Selected(menu: MenuItem) {
        Toast.makeText(this, menu.title, Toast.LENGTH_SHORT).show()
    }
}

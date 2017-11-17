package sample.neetoffice.com.neetannotation

import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import library.neetoffice.com.neetannotation.*
import library.nettoffice.com.restapi.ResponseCallBack

@NActivity(R.layout.activity_main)
@OptionsMenu(R.menu.mian)
class MainActivity : AnnotationCompatActivity() {
    @RestService
    lateinit var api: Api
    val responseCallBack = object : ResponseCallBack<String> {
        override fun onResponse(response: String) {
            Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
        }

        override fun onFailure(throwable: Throwable) {
            Toast.makeText(this@MainActivity, throwable.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    @Click
    fun button1Clicked(view: TextView) {
        Toast.makeText(this, view.text.toString(), Toast.LENGTH_SHORT).show()
        api.requestFromJson("39.7391536,-104.9847034").enqueue(responseCallBack)
    }

    @Click
    fun button2(view: TextView) {
        Toast.makeText(this, view.text.toString(), Toast.LENGTH_SHORT).show()
        api.requestFromXml().enqueue(responseCallBack)
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

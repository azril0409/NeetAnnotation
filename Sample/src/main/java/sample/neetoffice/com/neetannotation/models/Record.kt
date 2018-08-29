package sample.neetoffice.com.neetannotation.models

import library.neetoffice.com.neetannotation.Presenter
import library.neetoffice.com.neetdao.DatabaseField
import library.neetoffice.com.neetdao.DatabaseTable
import library.neetoffice.com.neetdao.Id
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@DatabaseTable
@Presenter
class Record() {
    companion object {
        @JvmStatic
        val FORMAT = SimpleDateFormat("MM/dd HH:mm:ss.SSS")
    }

    @Id
    var id: Int? = null
    @DatabaseField
    var time: Long = 0
    @DatabaseField
    var timeString: String = ""

    constructor(date: Date) : this() {
        time = date.time
        timeString = FORMAT.format(date)
    }

}
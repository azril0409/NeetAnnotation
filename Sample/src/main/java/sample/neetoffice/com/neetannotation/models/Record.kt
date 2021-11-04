package sample.neetoffice.com.neetannotation.models

import android.os.Parcel
import android.os.Parcelable
import library.neetoffice.com.neetannotation.Interactor
import library.neetoffice.com.neetannotation.ListInteractor
import library.neetoffice.com.neetannotation.SetInteractor
import library.neetoffice.com.neetdao.DatabaseField
import library.neetoffice.com.neetdao.DatabaseTable
import library.neetoffice.com.neetdao.Id
import java.text.SimpleDateFormat
import java.util.*

@DatabaseTable
@Interactor
@ListInteractor
@SetInteractor
class Record() : Parcelable {

    @Id
    var id: Int? = null

    @DatabaseField
    var time: Long = 0

    @DatabaseField
    var timeString: String = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        time = parcel.readLong()
        timeString = parcel.readString() ?: ""
    }

    constructor(date: Date) : this() {
        time = date.time
        val FORMAT = SimpleDateFormat("MM/dd HH:mm:ss.SSS")
        timeString = FORMAT.format(date)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeLong(time)
        parcel.writeString(timeString)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Record> {
        override fun createFromParcel(parcel: Parcel): Record {
            return Record(parcel)
        }

        override fun newArray(size: Int): Array<Record?> {
            return arrayOfNulls(size)
        }
    }

}
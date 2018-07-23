package sample.neetoffice.com.neetannotation.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import library.neetoffice.com.neetdao.DatabaseHelper
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.views.adapters.RecordAdapter

@Module
class RecordModule {
    @Provides
    fun DatabaseHelper(context: Context) = DatabaseHelper(context, "dvjuuiodgr.db", 1, Record::class.java)

    @Provides
    fun record(databaseHelper: DatabaseHelper) = databaseHelper.getDao(Record::class.java)

    @Provides
    fun adapter(context: Context) = RecordAdapter(context)
}
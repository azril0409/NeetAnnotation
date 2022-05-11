package sample.neetoffice.com.neetannotation.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import sample.neetoffice.com.neetannotation.models.Record
import sample.neetoffice.com.neetannotation.views.adapters.RecordAdapter

@Module
class RecordModule {

    @Provides
    fun injectRecord() = Record()

    @Provides
    fun adapter(context: Context) = RecordAdapter(context)
}
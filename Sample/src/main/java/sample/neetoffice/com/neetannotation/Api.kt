package sample.neetoffice.com.neetannotation

import library.nettoffice.com.restapi.*
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter

/**
 * Created by Deo-chainmeans on 2017/11/16.
 */
@RestApi(rootUrl = "https://maps.googleapis.com/maps/api", converters = arrayOf(StringHttpMessageConverter::class, GsonHttpMessageConverter::class))
interface Api {

    @Get("/elevation/json")
    @Accept(contentType = MediaType.APPLICATION_JSON)
    fun requestFromJson(@Field("locations") locations: String): Call<String>

    @Get("/elevation/xml?locations=39.7391536,-104.9847034")
    @Accept(contentType = MediaType.APPLICATION_XML)
    fun requestFromXml(): Call<String>
}
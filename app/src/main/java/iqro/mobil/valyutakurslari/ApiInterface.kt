package iqro.mobil.valyutakurslari

import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/uz/arkhiv-kursov-valyut/json/")
   suspend fun getData():Response<CurrencyData>
}
package com.boryans.covidstats.api

import android.util.ArrayMap
import com.boryans.covidstats.model.Country
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApi {

    @GET("cases")
    suspend fun getAllCountries(): Response<ArrayMap<String, Country>>

    @GET("cases?country=France")
     fun getSpecificCountry(@Query("country") country: String): Call<Country>

}
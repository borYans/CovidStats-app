package com.boryans.covidstats.api

import com.boryans.covidstats.model.Country
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApi {

    @GET("cases")
     fun getAllCountries(): Call<Map<String, Country.MainCountry>>

    @GET("cases?country=France")
     fun getSpecificCountry(@Query("country") country: String): Call<Country>

}
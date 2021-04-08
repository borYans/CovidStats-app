package com.boryans.covidstats.api

import com.boryans.covidstats.model.All
import com.boryans.covidstats.model.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApi {

    @GET("cases")
     fun getAllCountries(): Call<Map<String,All>>

    @GET("cases?country=France")
     fun getSpecificCountry(@Query("country") country: String): Call<Model>

}
package com.boryans.covidstats.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface CountriesApi {

    @GET("cases")
     fun getAllCountries(): Call<Map<All, Countries>>

    @GET("cases?country=France")
     fun getSpecificCountry(@Query("country") country: String): Call<Countries>

}
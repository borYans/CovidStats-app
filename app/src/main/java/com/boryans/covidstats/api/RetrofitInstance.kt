package com.boryans.covidstats.api

import com.boryans.covidstats.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {

        private val retrofit by lazy {

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val api: CountriesApi by lazy {
            retrofit.create(CountriesApi::class.java)
        }
    }
}

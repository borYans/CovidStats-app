package com.boryans.covidstats.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.db.CountryDatabase
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants
import com.boryans.covidstats.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class CovidStatsRepository(
    val db: CountryDatabase
) {

    fun getListOfAllCountries(): MutableLiveData<Resource<ArrayList<String>>> {
        val countriesLiveData: MutableLiveData<Resource<ArrayList<String>>> = MutableLiveData()
        val call = RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, Country>> {
            override fun onResponse(
                call: Call<Map<String, Country>>,
                response: Response<Map<String, Country>>
            ) {
                val map = response.body()

                val countryNames = ArrayList<String>(map?.keys!!)
                countriesLiveData.postValue(Resource.Success(countryNames))
            }

            override fun onFailure(call: Call<Map<String, Country>>, t: Throwable) {
                countriesLiveData.postValue(Resource.Error("Something went wrong."))
            }
        })
        return countriesLiveData
    }

    fun getSpecificCountry(country: String): MutableLiveData<Resource<Model>> {

        val modelLiveData: MutableLiveData<Resource<Model>> = MutableLiveData()
        val call = RetrofitInstance.API.getSpecificCountry(country)
        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val countriesResponse = response.body()

                        modelLiveData.postValue(Resource.Success(countriesResponse))

                    }
                } catch (e: Exception) {
                    Log.d(Constants.TAG, "Exception: $e")
                    modelLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                modelLiveData.postValue(null)
            }


        })
        return modelLiveData
    }




        suspend fun updateAndInsertCountry(country: Country) =
            db.getCountryDao().updateOrInsert(country)

        //suspend fun updateCache(mapCountries: Map<String, Country>) = db.getCountryDao().updateOrInsertCache(mapCountries)


        // fun getAllCountries() = db.getCountryDao().getAllCache()

        fun getFavoriteCountries() = db.getCountryDao().getFavoriteCountry()

        suspend fun deleteCountry(country: Country) = db.getCountryDao().deleteCountry(country)

    }

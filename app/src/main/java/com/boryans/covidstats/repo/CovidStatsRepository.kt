package com.boryans.covidstats.repo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.db.CountryDatabase
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.CountryData
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants
import com.boryans.covidstats.util.CountryApplication
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class CovidStatsRepository(
    val db: CountryDatabase
) {

    fun getListOfAllCountries(): MutableLiveData<Resource<ArrayList<Country>>> {
        val countries: MutableLiveData<Resource<ArrayList<Country>>> = MutableLiveData()

        val call = RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, Map<String, Country>>> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<Map<String, Map<String, Country>>>,
                response: Response<Map<String, Map<String, Country>>>
            ) {

                val map = response.body()

                val countriesList = ArrayList<Country>()

                for (entry in map?.entries!!) {
                    for (country in entry.value.entries) {
                        countriesList.add(country.value)
                        Log.d("", country.value.toString());
                    }
                }
                countries.postValue(Resource.Success(countriesList))


                CoroutineScope(Dispatchers.IO).launch {
                    saveAllCountries(countriesList)
                }
            }

            override fun onFailure(call: Call<Map<String, Map<String, Country>>>, t: Throwable) {
                countries.postValue(Resource.Error("Something went wrong."))
            }
        })
        return countries
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
                    modelLiveData.postValue(Resource.Error("Enter valid country name."))
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                modelLiveData.postValue(Resource.Error("Something went wrong."))
            }


        })
        return modelLiveData
    }

    suspend fun updateAndInsertCountry(country: Country) = db.getCountryDao().updateOrInsert(country)

    suspend fun saveAllCountries(listOfCountries: List<Country>) = db.getCountryDao().updateOrInsertCache(listOfCountries)

    suspend fun getAllCountries() = db.getCountryDao().getAllCountries()




    fun getFavoriteCountries() = db.getCountryDao().getFavoriteCountry()

    suspend fun deleteCountry(country: String) = db.getCountryDao().deleteCountry(country)





}

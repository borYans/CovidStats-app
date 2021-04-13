package com.boryans.covidstats.repo

import androidx.lifecycle.LiveData
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.db.CountryDatabase
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CovidStatsRepository(
    val db: CountryDatabase
) {


    fun getListOfAllCountries(): LiveData<Resource<ArrayList<String>>> {
      val call =  RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, Country>> {
            override fun onResponse(
                call: Call<Map<String, Country>>,
                response: Response<Map<String, Country>>
            ) {
                val map = response.body()

                val countryNames = ArrayList<String>(map?.keys!!)
                listOfAllCountries.postValue(Resource.Success(countryNames))
            }

            override fun onFailure(call: Call<Map<String, Country>>, t: Throwable) {

            }
        })

    }

    fun getSpecificCountry(country: String) =
        RetrofitInstance.API.getSpecificCountry(country)




   suspend fun updateAndInsertCountry(country: Country) = db.getCountryDao().updateOrInsert(country)

    //suspend fun updateCache(mapCountries: Map<String, Country>) = db.getCountryDao().updateOrInsertCache(mapCountries)




   // fun getAllCountries() = db.getCountryDao().getAllCache()

    fun getFavoriteCountries() = db.getCountryDao().getFavoriteCountry()

    suspend fun deleteCountry(country: Country) = db.getCountryDao().deleteCountry(country)

}
package com.boryans.covidstats.repo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.db.CountryDatabase
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants
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
        val countriesLiveData: MutableLiveData<Resource<ArrayList<Country>>> = MutableLiveData()
        val call = RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, Country>> {
            override fun onResponse(
                call: Call<Map<String, Country>>,
                response: Response<Map<String, Country>>
            ) {
                val map = response.body()
                val countryNames = ArrayList<Country>(map?.values!!)
                countriesLiveData.postValue(Resource.Success(countryNames))

                CoroutineScope(Dispatchers.IO).launch {
                    updateCache(countryNames)
                }
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

    suspend fun updateAndInsertCountry(country: Country) = db.getCountryDao().updateOrInsert(country)

    suspend fun updateCache(listOfCountries: List<Country>) = db.getCountryDao().updateOrInsertCache(listOfCountries)

    fun getAllCountries() = db.getCountryDao().getAllCountries()

    fun getFavoriteCountries() = db.getCountryDao().getFavoriteCountry()

    suspend fun deleteCountry(country: Country) = db.getCountryDao().deleteCountry(country)


     fun hasInternetConnection(connectivityManager: ConnectivityManager): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> true

                }
            }
        }
        return false
    }


}

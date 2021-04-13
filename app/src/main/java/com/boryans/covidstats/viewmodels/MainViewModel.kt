package com.boryans.covidstats.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.repo.CovidStatsRepository
import com.boryans.covidstats.util.Constants
import com.boryans.covidstats.util.CountryApplication
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error
import java.lang.Exception

class MainViewModel(
    val app: Application,
    val repository: CovidStatsRepository
): AndroidViewModel(app) {

    val countryDetails: MutableLiveData<Resource<Model>> = MutableLiveData() // must be immutable
    val listOfAllCountries: MutableLiveData<Resource<ArrayList<String>>> = MutableLiveData()
    val error: MutableLiveData<Resource<Error>> = MutableLiveData()


    fun getListOfAllCountries()  {
        listOfAllCountries.postValue(Resource.Loading())
        handleCountryListResponse()

    }

    fun getSingleCountry(countryName: String) = viewModelScope.launch {
        countryDetails.postValue(Resource.Loading())
        handleSingleCountry(countryName)
    }


    private fun handleCountryListResponse() {
        if (true) {
            val call = repository.getListOfAllCountries()
            call.enqueue(object : Callback<Map<String,Country>>{
                override fun onResponse(
                    call: Call<Map<String, Country>>,
                    response: Response<Map<String, Country>>
                ) {
                    val map = response.body()

                    val countryNames = ArrayList<String>(map?.keys!!)
                    listOfAllCountries.postValue(Resource.Success(countryNames))
                }

                override fun onFailure(call: Call<Map<String, Country>>, t: Throwable) {
                    error.postValue(Resource.Error("Something went wrong."))
                }
            })
        } else {
          //  fetchAllCountriesFromLocal()
        }
    }

   // private fun fetchAllCountriesFromLocal() = repository.getAllCountries()


    private fun handleSingleCountry(country: String) {
        if (true) {
            val call = repository.getSpecificCountry(country)
            call.enqueue(object : Callback<Model> {
                override fun onResponse(call: Call<Model>, response: Response<Model>) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val countriesResponse = response.body()

                            countryDetails.postValue(Resource.Success(countriesResponse))

                        }
                    } catch (e: Exception) {
                        Log.d(Constants.TAG, "Exception: $e")
                        countryDetails.postValue(Resource.Error("Country name is not valid."))
                    }
                }

                override fun onFailure(call: Call<Model>, t: Throwable) {
                    error.postValue(Resource.Error("Something went wrong."))
                }


            })
        } else {
            fetchSingleCountryFromLocal()
        }
    }

    private fun fetchSingleCountryFromLocal() {
        //doSomething
    }

    fun saveCountry(country: Country) = viewModelScope.launch {
        repository.updateAndInsertCountry(country)
    }

  /*  fun saveCache(country: Map<String, Country>) = viewModelScope.launch {
        repository.updateCache(country)
    }

   */


    fun getFavoriteCountries() = repository.getFavoriteCountries()

   // fun getListOfCountriesFromLocal() = repository.getAllCountries()


    fun deleteCountry(country: Country) = viewModelScope.launch {
        repository.deleteCountry(country)
    }



    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<CountryApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
                    else -> false

                }
            }
        }
        return false
    }


}
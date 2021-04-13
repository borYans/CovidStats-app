package com.boryans.covidstats.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    val repository: CovidStatsRepository
): ViewModel() {

    var countryDetails: MutableLiveData<Resource<Model>> = MutableLiveData() // must be immutable
    var listOfAllCountries: MutableLiveData<Resource<ArrayList<String>>> = MutableLiveData()
    val error: MutableLiveData<Resource<Error>> = MutableLiveData()


    fun getListOfAllCountries() {
        listOfAllCountries.postValue(Resource.Loading())
        listOfAllCountries = handleCountryListResponse()
    }

    fun getSingleCountry(countryName: String){
         countryDetails.postValue(Resource.Loading())
         countryDetails = handleSingleCountry(countryName)
    }

    private fun handleCountryListResponse() = repository.getListOfAllCountries()
    private fun handleSingleCountry(country: String) = repository.getSpecificCountry(country)

    fun getFavoriteCountries() = repository.getFavoriteCountries()

    fun saveCountry(country: Country) = viewModelScope.launch {
        repository.updateAndInsertCountry(country)
    }

    fun deleteCountry(country: Country) = viewModelScope.launch {
        repository.deleteCountry(country)
    }





  /*  fun saveCache(country: Map<String, Country>) = viewModelScope.launch {
        repository.updateCache(country)
    }

   */

    // private fun fetchAllCountriesFromLocal() = repository.getAllCountries()



   // fun getListOfCountriesFromLocal() = repository.getAllCountries()





   /* private fun hasInternetConnection(): Boolean {
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

    */


}
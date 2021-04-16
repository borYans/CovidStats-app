package com.boryans.covidstats.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.repo.CovidStatsRepository
import com.boryans.covidstats.util.CountryApplication
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: CovidStatsRepository,
    app: Application
): AndroidViewModel(app) {

    var countryDetails: MutableLiveData<Resource<Model>> = MutableLiveData() // must be immutable
    var listOfAllCountries: MutableLiveData<Resource<ArrayList<String>>> = MutableLiveData()
    var listOfCountriesFromRemote: MutableLiveData<List<Country>> = MutableLiveData()
    var internetConnection: MutableLiveData<Resource<Boolean>> = MutableLiveData()






     fun refreshUI()  {
         listOfAllCountries.postValue(Resource.Loading())


         if (hasInternetConnection()) {
             listOfAllCountries = repository.getListOfAllCountries()
         } else {
             getAllCountriesFromLocal()
         }
    }

    fun getSingleCountry(countryName: String){
         countryDetails.postValue(Resource.Loading())
         countryDetails = repository.getSpecificCountry(countryName)
    }


    fun getFavoriteCountries() = repository.getFavoriteCountries()

    fun saveCountry(country: Country) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateAndInsertCountry(country)
    }

    fun deleteCountry(country: String) = viewModelScope.launch {
        repository.deleteCountry(country)
    }

    private fun getAllCountriesFromLocal() = viewModelScope.launch {
        listOfCountriesFromRemote.postValue(repository.getAllCountries())
    }


    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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





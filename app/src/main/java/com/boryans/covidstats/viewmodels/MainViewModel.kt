package com.boryans.covidstats.viewmodels

import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.repo.CovidStatsRepository
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.launch
import java.lang.Error

class MainViewModel(
    val repository: CovidStatsRepository,
    val connectivityManager: ConnectivityManager
): ViewModel() {

    var countryDetails: MutableLiveData<Resource<Model>> = MutableLiveData() // must be immutable
    var listOfAllCountries: MutableLiveData<Resource<ArrayList<Country>>> = MutableLiveData()
    var internetConnection: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val error: MutableLiveData<Resource<Error>> = MutableLiveData()


    fun checkInternetConnection()  {
        internetConnection.postValue(Resource.Success(repository.hasInternetConnection(connectivityManager)))
    }


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

    fun getAllCountriesFromLocal() = repository.getAllCountries()





}
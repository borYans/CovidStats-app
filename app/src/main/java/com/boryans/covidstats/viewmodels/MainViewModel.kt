package com.boryans.covidstats.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.repo.CovidStatsRepository
import com.boryans.covidstats.util.Constants
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
): ViewModel() {

    val countryDetails: MutableLiveData<Resource<Country>> = MutableLiveData() // must be immutable
    val listOfAllCountries: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val error: MutableLiveData<Resource<Error>> = MutableLiveData()

    fun getListOfAllCountries()  { //ktx live data

        listOfAllCountries.postValue(Resource.Loading()) //show progress bar
        val call = repository.getListOfAllCountries()
        call.enqueue(object : Callback<Map<String, Country>> {
            override fun onResponse(
                call: Call<Map<String, Country>>,
                response: Response<Map<String, Country>>
            ) {
                val map = response.body()?.keys?.toList()
                listOfAllCountries.postValue(Resource.Success(map))

                /*for(country in map?.entries!!) {
                    val childMap = country.value
                    for (region in childMap.entries) {
                        countryNames.add(region.key)
                    }
                }

                 */
            }

            override fun onFailure(call: Call<Map<String, Country>>, t: Throwable) {

            }

        })
    }

    fun getCountryDetailsData(countryName: String) = viewModelScope.launch {

        countryDetails.postValue(Resource.Loading()) //show progress bar

        val call = repository.getSpecificCountry(countryName)
        call.enqueue(object : Callback<Country> {
            override fun onResponse(call: Call<Country>, response: Response<Country>) {
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

            override fun onFailure(call: Call<Country>, t: Throwable) {

            }


        })
    }

    fun saveCountry(country: Country) = viewModelScope.launch {
        repository.updateAndInsertCountry(country)
    }

    fun getFavoriteCountries() = repository.getFavoriteCountries()

    fun deleteCountry(country: Country) = viewModelScope.launch {
        repository.deleteCountry(country)
    }
}
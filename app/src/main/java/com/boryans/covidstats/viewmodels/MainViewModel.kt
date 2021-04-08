package com.boryans.covidstats.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.model.All
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error
import java.lang.Exception

class MainViewModel: ViewModel() {

    val countryDetails: MutableLiveData<Resource<Model>> = MutableLiveData()
    val listOfAllCountries: MutableLiveData<Resource<ArrayList<String>>> = MutableLiveData()
    val error: MutableLiveData<Resource<Error>> = MutableLiveData()

    fun getListOfAllCountries()  {
        listOfAllCountries.postValue(Resource.Loading()) //show progress bar

        val call = RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, All>>{
            override fun onResponse(
                call: Call<Map<String, All>>,
                response: Response<Map<String, All>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val map = response.body()
                    val countryNames = ArrayList<String>(map?.keys!!)
                    listOfAllCountries.postValue(Resource.Success(countryNames))

                } else {
                    Log.d(Constants.TAG, "Response: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Map<String, All>>, t: Throwable) {
                error.postValue(Resource.Error("Something went wrong."))
            }
        })
    }

    fun getCountryDetailsData(countryName: String) = viewModelScope.launch {

        countryDetails.postValue(Resource.Loading()) //show progress bar

        val call = RetrofitInstance.API.getSpecificCountry(countryName)
        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val countriesResponse = response.body()

                        countryDetails.postValue(Resource.Success(countriesResponse))

                    }
                } catch (e: Exception) {
                    Log.d(Constants.TAG, "Exception: $e")
                    countryDetails.postValue(Resource.Error("Country name is not valid. "))
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                listOfAllCountries.postValue(Resource.Error("Something went wrong."))

            }

        })
    }
}
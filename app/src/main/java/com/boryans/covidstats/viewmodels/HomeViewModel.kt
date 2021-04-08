package com.boryans.covidstats.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants.Companion.TAG
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class HomeViewModel: ViewModel() {


    val countryDetails: MutableLiveData<Resource<Model>> = MutableLiveData()

    fun getCountryDetailsData(countryName: String) = viewModelScope.launch {

        val call = RetrofitInstance.API.getSpecificCountry(countryName)
        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val countriesResponse = response.body()

                        countryDetails.postValue(Resource.Success(countriesResponse))

                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Exception: $e")
                    countryDetails.postValue(Resource.Error("Country name is not valid. "))
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }



}
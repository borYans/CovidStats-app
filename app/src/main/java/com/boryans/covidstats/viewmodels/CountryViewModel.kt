package com.boryans.covidstats.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.util.Constants.Companion.SUCCESS
import com.boryans.covidstats.util.Constants.Companion.TAG
import com.boryans.covidstats.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Error

class CountryViewModel : ViewModel() {

    val listOfAllCountries: MutableLiveData<Resource<ArrayList<String>>> = MutableLiveData()
    val error: MutableLiveData<Resource<Error>> = MutableLiveData()



    fun getListOfAllCountries()  {

        val call = RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, Country.MainCountry>>{
            override fun onResponse(
                call: Call<Map<String, Country.MainCountry>>,
                response: Response<Map<String, Country.MainCountry>>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    val map = response.body()
                    val countryNames = ArrayList<String>()
                    for (key in map!!.keys) {
                        countryNames.add(key)
                        Log.d(SUCCESS, "Country: $key")
                    }

                     listOfAllCountries.postValue(Resource.Success(countryNames))
                } else {
                    Log.d(TAG, "Nesto ne ti e okejjj!!!")
                }
            }

            override fun onFailure(call: Call<Map<String, Country.MainCountry>>, t: Throwable) {

            }


        })
    }
}

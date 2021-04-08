package com.boryans.covidstats.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.model.All
import com.boryans.covidstats.model.Model
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


    fun getListOfAllCountries() {

        val call = RetrofitInstance.API.getAllCountries()
        call.enqueue(object : Callback<Map<String, Model>> {
            override fun onResponse(
                call: Call<Map<String, Model>>,
                response: Response<Map<String, Model>>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    val map = response.body()
                    val all = map?.get("All")?.all
                    val region = map?.get("Region")?.regionInfo

                    val countryNames = ArrayList<String>(all?.keys)
                    val regionNames = ArrayList<String>(region?.keys)

                    val merged = ArrayList<String>()
                    merged.apply {
                        addAll(countryNames)
                        addAll(regionNames)
                    }

                    listOfAllCountries.postValue(Resource.Success(merged))
                } else {
                    Log.d(TAG, "Nesto ne ti e okejjj!!!")
                }
            }

            override fun onFailure(call: Call<Map<String, Model>>, t: Throwable) {
            }

        })
    }
}

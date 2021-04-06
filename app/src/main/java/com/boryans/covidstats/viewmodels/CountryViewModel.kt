package com.boryans.covidstats.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.util.Resource
import kotlinx.coroutines.launch

class CountryViewModel: ViewModel() {


    val listOfAllCountries : MutableLiveData<Resource<ArrayList<Country>>> = MutableLiveData()


    fun getListOfAllCountries() = viewModelScope.launch {

        val response = RetrofitInstance.API.getAllCountries()
        if (response.isSuccessful && response.body() != null) {
            val  countries = response.body()
            val arrayList = ArrayList(countries?.values)
            listOfAllCountries.postValue(Resource.Success(arrayList))
        }

    }
}
package com.boryans.covidstats.repo

import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.db.CountryDatabase


class CovidStatsRepository(
    val db: CountryDatabase
) {

    fun getListOfAllCountries() = RetrofitInstance.API.getAllCountries()

    fun getSpecificCountry(country: String) =
        RetrofitInstance.API.getSpecificCountry(country)




}
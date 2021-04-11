package com.boryans.covidstats.repo

import com.boryans.covidstats.api.RetrofitInstance
import com.boryans.covidstats.db.CountryDatabase
import com.boryans.covidstats.model.Country


class CovidStatsRepository(
    val db: CountryDatabase
) {

    fun getListOfAllCountries() = RetrofitInstance.API.getAllCountries()

    fun getSpecificCountry(country: String) =
        RetrofitInstance.API.getSpecificCountry(country)

   suspend fun updateAndInsertCountry(country: Country) = db.getCountryDao().updateOrInsert(country)

    fun getFavoriteCountries() = db.getCountryDao().getAllFavoritesCountries()

    suspend fun deleteCountry(country: Country) = db.getCountryDao().deleteCountry(country)

}
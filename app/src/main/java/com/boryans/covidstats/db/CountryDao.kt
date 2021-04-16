package com.boryans.covidstats.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boryans.covidstats.model.Country

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(country: Country)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertCache(countries: List<Country>)

    @Query("SELECT * FROM covid_statistics WHERE isFavorite = 1")
    fun getFavoriteCountry(): LiveData<List<Country>>

    @Query("SELECT * FROM covid_statistics")
    suspend fun getAllCountries(): List<Country>

    @Query("DELETE FROM covid_statistics WHERE country = :country")
    suspend fun deleteCountry(country: String)


}
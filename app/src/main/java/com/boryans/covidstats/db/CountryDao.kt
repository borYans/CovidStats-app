package com.boryans.covidstats.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boryans.covidstats.model.Country

@Dao
interface CountryDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(country: Country): Long

    @Query("SELECT * FROM covid_statistics")
    fun getAllFavoritesCountries(): LiveData<List<Country>>

    @Delete
    suspend fun deleteCountry(country: Country)



}
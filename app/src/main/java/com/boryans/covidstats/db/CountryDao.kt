package com.boryans.covidstats.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boryans.covidstats.model.Country

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsert(country: Country): Long

    /*@Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateOrInsertCache(mapCountry: Map<String, Country>):Long

    @Query("SELECT * FROM covid_statistics")
    fun getAllCache(): LiveData<List<Map<String, Country>>>

    */

    @Query("SELECT * FROM covid_statistics WHERE isFavorite = 1")
    fun getFavoriteCountry(): LiveData<List<Country>>

    @Delete
    suspend fun deleteCountry(country: Country)

}
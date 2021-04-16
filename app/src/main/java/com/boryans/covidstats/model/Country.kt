package com.boryans.covidstats.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "covid_statistics")
data class Country(
    @PrimaryKey(autoGenerate = true)
    var countryId: Int = 0,
    var abbreviation: String? = "",
    @SerializedName("capital_city")
    var capitalCity: String? = "",
    var confirmed: Int = 0,
    var continent: String? = "",
    var country: String? = "",
    var deaths: Int = 0,
   // @SerializedName("elevation_in_meters")
   // var elevationInMeters: Int = 0,
    var iso: Int = 0,
    var lat: String = "",
    @SerializedName("life_expectancy")
    var lifeExpectancy: String? = "",
    var location: String? = "",
    var long: String? = "",
    var population: Long = 0,
    var recovered: Int = 0,
    @SerializedName("sq_km_area")
    var sqKmArea: Double = 0.00,
    var updated: String? = "",
    var isFavorite: Boolean = false
)
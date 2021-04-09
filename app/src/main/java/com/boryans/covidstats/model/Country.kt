package com.boryans.covidstats.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "covid_statistics")
data class Country(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var abbreviation: String = "",
    @SerializedName("capital_city")
    var capitalCity: String = "",
    var confirmed: Int = 0,
    var continent: String = "",
    var country: String = "",
    var deaths: Int = 0,
    @SerializedName("elevation_in_meters")
    var elevationInMeters: Int = 0,
    var iso: Int = 0,
    var lat: String = "",
    @SerializedName("life_expectancy")
    var lifeExpectancy: String = "",
    var location: String = "",
    var long: String? = "",
    var population: Int = 0,
    var recovered: Int = 0,
    @SerializedName("sq_km_area")
    var sqKmArea: Int = 0,
    var updated: String = ""
)
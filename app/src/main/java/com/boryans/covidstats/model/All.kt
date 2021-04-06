package com.boryans.covidstats.model


import com.google.gson.annotations.SerializedName

data class All(
    val abbreviation: String,
    @SerializedName("capital_city")
    val capitalCity: String,
    val confirmed: Int,
    val continent: String,
    val country: String,
    val deaths: Int,
    @SerializedName("elevation_in_meters")
    val elevationInMeters: Int,
    val iso: Int,
    val lat: String,
    @SerializedName("life_expectancy")
    val lifeExpectancy: String,
    val location: String,
    val long: String,
    val population: Int,
    val recovered: Int,
    @SerializedName("sq_km_area")
    val sqKmArea: Int,
    val updated: String
)
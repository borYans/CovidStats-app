package com.boryans.covidstats.model


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("All")
    val all: All
)
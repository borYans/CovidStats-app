package com.boryans.covidstats.api


import com.google.gson.annotations.SerializedName

data class Countries(
    @SerializedName("All")
    val all: All
)
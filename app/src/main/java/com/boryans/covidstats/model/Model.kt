package com.boryans.covidstats.model


import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("All")
    val all: All,
    val regionInfo: RegionInfo
)
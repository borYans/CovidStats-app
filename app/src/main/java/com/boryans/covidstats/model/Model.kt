package com.boryans.covidstats.model


import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("All")
    val country: Country

)
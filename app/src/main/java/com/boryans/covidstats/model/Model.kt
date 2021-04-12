package com.boryans.covidstats.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("All")
    @Expose
    val country: Country

)
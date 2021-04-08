package com.boryans.covidstats.util

import java.util.*

object CountryValidationUtil {

    private val countries = listOf("Serbia, US, Bosnia and Herzegovina, Montenegro")
    /**
     * It will fail if:
     * 1. Empty string
     * 2. If does not exists in the api.
     * 3. Whitespace " word"
     */
    fun validateCountryNameInput(country: String):Boolean {
        if (country.isEmpty()) return false
        if (country.isBlank()) return false
        if (country.contains(" ")) return false
        if (country !in countries) return false

        return true
    }
}
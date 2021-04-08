package com.boryans.covidstats.viewmodels

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel

    private val fakeAPICountryNames = listOf("Serbia, US, Bosnia and Herzegovina, Montenegro, Croatia, Bulgaria")

    @Before
    fun setup() {
        mainViewModel = MainViewModel()
    }

    @Test
    fun `pass wrong parameter to api query`() {
        mainViewModel.getCountryDetailsData("DisneyLand")
       // val value = mainViewModel.countryDetails.getOrAwaitValue()
    }
}
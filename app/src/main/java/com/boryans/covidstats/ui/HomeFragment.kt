package com.boryans.covidstats.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.adapters.HomeRecyclerAdapter
import com.boryans.covidstats.api.All
import com.boryans.covidstats.api.Countries
import com.boryans.covidstats.api.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_country.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(R.layout.fragment_home) {

    val TAG = "RESPONSE"
    val SUCCESS = "SUCCESS"



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            setStatsVisibilityToGone()
            if (searchInputTxt.text?.trim().toString().isBlank()) {
                Snackbar.make(requireView(), "Add name of the country.", Snackbar.LENGTH_SHORT).show()

            } else {
                fetchCountries(searchInputTxt.text?.trim().toString())
            }

        }

    }

    private fun fetchCountries(countryName:String) {

        val call = RetrofitInstance.api.getSpecificCountry(countryName)
        call.enqueue(object : Callback<Countries> {
            override fun onResponse(call: Call<Countries>, response: Response<Countries>) {
                try {
                    if (response.isSuccessful && response.body() != null) {
                        val country = response.body()
                        progressBar.visibility = View.GONE
                        appendDataToViews(country!!)
                        setStatsVisibilityToVisible()
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Exception: $e")
                    Toast.makeText(requireContext(), "Not valid country name.", Toast.LENGTH_SHORT).show()
                }


            }

            override fun onFailure(call: Call<Countries>, t: Throwable) {
                Log.d(TAG, "Error response: $t")

            }

        })

    }

    private fun appendDataToViews(country: Countries) {
        statsTextView.text = country?.all?.country
        totalCases.text = "Total cases: ${country?.all?.confirmed?.toString()}"
        recoveredCases.text = "Recoveres cases: ${country?.all?.recovered?.toString()}"
        deaths.text = "Deaths: ${country?.all?.deaths?.toString()}"
        lifeExpectancy.text = "Life expectancy: ${country?.all?.lifeExpectancy} years"
        lastUpdated.text = "Last updated: ${country?.all?.updated}"
    }

    private fun setStatsVisibilityToVisible() {
        statsTextView.visibility = View.VISIBLE
        totalCases.visibility = View.VISIBLE
        recoveredCases.visibility = View.VISIBLE
        deaths.visibility = View.VISIBLE
        lifeExpectancy.visibility = View.VISIBLE
        lastUpdated.visibility = View.VISIBLE
    }

    private fun setStatsVisibilityToGone() {
        statsTextView.visibility = View.VISIBLE
        totalCases.visibility = View.VISIBLE
        recoveredCases.visibility = View.VISIBLE
        deaths.visibility = View.VISIBLE
        lifeExpectancy.visibility = View.VISIBLE
        lastUpdated.visibility = View.VISIBLE
    }



}
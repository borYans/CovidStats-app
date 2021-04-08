package com.boryans.covidstats.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.boryans.covidstats.R
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.setSupportActionBar(my_toolbar)
        setHasOptionsMenu(true)


        homeViewModel.countryDetails.observe(viewLifecycleOwner, { countryDetails ->
            when (countryDetails) {
                is Resource.Success -> {
                    countryDetails.let { details ->
                        setStatsVisibilityToVisible()
                        progressBar.visibility = View.GONE
                        appendDataToViews(details)
                    }
                }
                is Resource.Error -> {
                    Snackbar.make(
                        requireView(),
                        "Something went wrong. Check internet connection.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        })

        searchButton.setOnClickListener {
            setStatsVisibilityToGone()
            progressBar.visibility = View.VISIBLE


            if (searchInputTxt.text?.trim().toString().isBlank()) {
                Snackbar.make(requireView(), "Add name of the country.", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                homeViewModel.getCountryDetailsData(searchInputTxt.text?.trim().toString())
            }
        }

        my_toolbar.inflateMenu(R.menu.home_toolbar_menu)
        my_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.listOfInfectedCountries -> Navigation.findNavController(requireView()).navigate(HomeFragmentDirections.actionHomeFragmentToCountryList())
            }
            true
        }
    }

    private fun appendDataToViews(details: Resource.Success<Model>) {
        statsTextView.text = details.data?.all?.country
        totalCases.text = "Total cases: ${details.data?.all?.confirmed}"
        recoveredCases.text = "Recovered cases: ${details.data?.all?.recovered.toString()}"
        deaths.text = "Deaths: ${details.data?.all?.deaths?.toString()}"
        lifeExpectancy.text = "Life expectancy: ${details.data?.all?.lifeExpectancy} years"
        lastUpdated.text = "Last updated: ${details.data?.all?.updated}"
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
        statsTextView.visibility = View.INVISIBLE
        totalCases.visibility = View.INVISIBLE
        recoveredCases.visibility = View.INVISIBLE
        deaths.visibility = View.INVISIBLE
        lifeExpectancy.visibility = View.INVISIBLE
        lastUpdated.visibility = View.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_toolbar_menu, menu)
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

}
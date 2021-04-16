package com.boryans.covidstats.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.boryans.covidstats.R
import com.boryans.covidstats.activities.MainActivity
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(R.layout.fragment_home) {


    private lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.setSupportActionBar(my_toolbar)
        setHasOptionsMenu(true)
        mainViewModel = (activity as MainActivity).mainViewModel

        my_toolbar.inflateMenu(R.menu.home_toolbar_menu)
        my_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.listOfInfectedCountries -> Navigation.findNavController(requireView()).navigate(HomeFragmentDirections.actionHomeFragmentToCountryList())
                R.id.myCountries -> Navigation.findNavController((requireView())).navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteCountry())
            }
            true
        }



        mainViewModel.countryDetails.observe(viewLifecycleOwner, { countryDetails ->
            when (countryDetails) {
                is Resource.Success -> {
                    hideProgressBar()
                    countryDetails.let { details ->
                        setStatsVisibilityToVisible()
                        statsTextView.text = details.data?.country?.country
                        totalCases.text = "Total cases: ${details.data?.country?.confirmed.toString()}"
                        recoveredCases.text = "Recovered cases: ${details.data?.country?.recovered.toString()}"
                        deaths.text = "Deaths: ${details.data?.country?.deaths?.toString()}"
                        lifeExpectancy.text = "Life expectancy: ${details.data?.country?.lifeExpectancy} years"
                        lastUpdated.text = "Last updated: ${details.data?.country?.updated ?: "X"}"
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(
                        requireView(),
                        "Something went wrong. Check internet connection.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                   showProgessBar()
                }
            }
        })

        searchButton.setOnClickListener {
            setStatsVisibilityToGone()
            showProgessBar()
            if (searchInputTxt.text?.trim().toString().isBlank()) {
                Snackbar.make(requireView(), "Add name of the country.", Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                mainViewModel.getSingleCountry(searchInputTxt.text?.trim().toString())
            }
        }

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

    private fun showProgessBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

}
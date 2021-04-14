package com.boryans.covidstats.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.activities.MainActivity
import com.boryans.covidstats.adapters.CountryListRecyclerAdapter
import com.boryans.covidstats.listeners.CountryClickListener
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.util.Constants.Companion.TAG
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.details_country_dialog.*
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.android.synthetic.main.fragment_home.*


class CountryList : Fragment(R.layout.fragment_country_list), CountryClickListener {

    private lateinit var countryRecyclerAdapter: CountryListRecyclerAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = (activity as MainActivity).mainViewModel
        setUpRecyclerView()


        mainViewModel.checkInternetConnection()
        mainViewModel.internetConnection.observe(viewLifecycleOwner, { connection ->
            when(connection) {
                is Resource.Success -> {

                    fetchFromRemote()
                    mainViewModel.listOfAllCountries.observe(viewLifecycleOwner, { listOfCountries ->
                        when (listOfCountries) {
                            is Resource.Success -> {
                                hideProgressBar()
                                listOfCountries.let { list ->
                                    list.data?.let { countries ->
                                        val listOfCountryNames = ArrayList<String>()
                                        for (country in countries) {
                                            listOfCountryNames.add(country.country)
                                        }
                                        countryRecyclerAdapter.updateCountriesList(listOfCountryNames)
                                        Log.d(TAG, countries.toString())
                                    }
                                }
                            }
                            is Resource.Error -> {
                                hideProgressBar()
                            }

                            is Resource.Loading -> {
                                showProgressBar()
                            }
                        }
                    })
                }
                else -> {
                    fetchFromCountryDatabase()
                    mainViewModel.getAllCountriesFromLocal().observe(viewLifecycleOwner, { listOfCountriesFromLocal ->
                        hideProgressBar()
                        val listOfcountries = ArrayList<String>()
                        for (country in listOfCountriesFromLocal) {
                            listOfcountries.add(country.country)
                        }
                        countryRecyclerAdapter.updateCountriesList(listOfcountries)
                    })

                }
            }
        })




    }

    private fun setUpRecyclerView() {

        countryRecyclerView.apply {
            countryRecyclerAdapter = CountryListRecyclerAdapter(this@CountryList)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryRecyclerAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCountryClick(countryName: String) {
        Navigation.findNavController(requireView()).navigate(CountryListDirections.actionCountryListToCountryDetailsStatsFragment(countryName))
    }

    private fun showProgressBar() {
        recyclerProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        recyclerProgressBar.visibility = View.INVISIBLE
    }
     private fun fetchFromRemote() = mainViewModel.getListOfAllCountries()

     private fun fetchFromCountryDatabase() = mainViewModel.getAllCountriesFromLocal()

    }


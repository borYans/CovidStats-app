package com.boryans.covidstats.ui

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.ui.adapters.CountryListRecyclerAdapter
import com.boryans.covidstats.listeners.CountryClickListener
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

        mainViewModel.refreshUI()
        showProgressBar()

        mainViewModel.listOfAllCountries.observe(
            viewLifecycleOwner,
            { listOfCountries ->
                when (listOfCountries) {
                    is Resource.Success -> {

                        hideProgressBar()

                        listOfCountries.let { list ->
                            list.data?.let { countries ->

                                val countryListForRecyclerView = ArrayList<String>()
                                for (country in countries) {
                                    countryListForRecyclerView.add(country.country!!)
                                }

                                countryRecyclerAdapter.updateCountriesList(countryListForRecyclerView)
                                filterRecyclerView(countryListForRecyclerView)


                                Log.d(TAG, countries.toString())
                            }
                        }
                    }
                }
            })


        mainViewModel.listOfAllCountriesFromLocal.observe(viewLifecycleOwner, { listOfCountriesFromLocal ->
            hideProgressBar()
            val listOfcountries = ArrayList<String>()
            for (country in listOfCountriesFromLocal) {
                listOfcountries.add(country.country!!)
            }
            countryRecyclerAdapter.updateCountriesList(listOfcountries)
        })

    }

    private fun filterRecyclerView(listOfCountries: ArrayList<String>) {
        searchRecycler.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

            }

            override fun afterTextChanged(s: Editable?) {
                val newFilteredList = ArrayList<String>()
                for (country in listOfCountries) {
                    if (country.toLowerCase().contains(s.toString().toLowerCase())) {
                        newFilteredList.add(country)
                    }
                }
                countryRecyclerAdapter.updateCountriesList(newFilteredList)
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
        Navigation.findNavController(requireView()).navigate(
            CountryListDirections.actionCountryListToCountryDetailsStatsFragment(countryName)
        )
    }

    private fun showProgressBar() {
        recyclerProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        recyclerProgressBar.visibility = View.INVISIBLE
    }
}


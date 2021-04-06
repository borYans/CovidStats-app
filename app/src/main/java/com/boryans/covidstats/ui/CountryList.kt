package com.boryans.covidstats.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.adapters.CountryListRecyclerAdapter
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country_list.*


class CountryList : Fragment(R.layout.fragment_country_list) {

    private val countryRecyclerAdapter = CountryListRecyclerAdapter()
    private val countryViewModel: CountryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        countryViewModel.listOfAllCountries.observe(viewLifecycleOwner, { listOfCountries ->
            when(listOfCountries) {
                is Resource.Success -> {
                    listOfCountries.let { list ->
                       list.data?.let { countries ->
                               countryRecyclerAdapter.updateCountriesList(countries)
                       }
                    }
                }
            }

        })
    }

    private fun setUpRecyclerView() {
        countryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryRecyclerAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        countryViewModel.getListOfAllCountries()
    }

}
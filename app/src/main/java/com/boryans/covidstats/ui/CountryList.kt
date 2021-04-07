package com.boryans.covidstats.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.adapters.CountryListRecyclerAdapter
import com.boryans.covidstats.util.Constants.Companion.TAG
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.CountryViewModel
import kotlinx.android.synthetic.main.fragment_country_list.*


class CountryList : Fragment(R.layout.fragment_country_list) {

    private lateinit var countryRecyclerAdapter : CountryListRecyclerAdapter
    private val countryViewModel: CountryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        Log.d(TAG, "Fragment started!")

        countryViewModel.getListOfAllCountries()
        countryViewModel.listOfAllCountries.observe(viewLifecycleOwner, { listOfCountries ->
            when(listOfCountries) {
                is Resource.Success -> {
                    listOfCountries.let { list ->
                       list.data?.let { countries ->
                               countryRecyclerAdapter.updateCountriesList(countries)
                           Log.d(TAG, countries.toString())
                       }
                    }
                }

                is Resource.Error -> {
                    listOfCountries.let { message ->
                        Toast.makeText(requireContext(), listOfCountries.message , Toast.LENGTH_SHORT).show()
                    }

                }
            }

        })

    }

    private fun setUpRecyclerView() {

        countryRecyclerView.apply {
            countryRecyclerAdapter = CountryListRecyclerAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryRecyclerAdapter
        }
    }


}
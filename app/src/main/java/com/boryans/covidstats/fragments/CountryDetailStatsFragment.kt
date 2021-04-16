package com.boryans.covidstats.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.boryans.covidstats.R
import com.boryans.covidstats.activities.MainActivity
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_country_details_stats.*


class CountryDetailStatsFragment : Fragment(R.layout.fragment_country_details_stats) {


    private lateinit var mainViewModel: MainViewModel
    private lateinit var country: Country


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = (activity as MainActivity).mainViewModel


        addToFavoritesFab.setOnClickListener {
            country.let {
                country.isFavorite = true
                mainViewModel.saveCountry(it)
            }
          Snackbar.make(it, "${country.country} added to favorites.", Snackbar.LENGTH_SHORT).show()
        }


        arguments?.let {
            val args = CountryDetailStatsFragmentArgs.fromBundle(it)
            mainViewModel.getSingleCountry(args.countryName)
        }



        mainViewModel.countryDetails.observe(viewLifecycleOwner, { countryDetails ->
            when (countryDetails) {
                is Resource.Success -> {

                    countryDetails.let { details ->
                        details.data.let { model ->
                            countryNameDetails.text = model?.country?.country
                            confirmedCasesNumber.text = model?.country?.confirmed.toString()
                            deathsCasesNumber.text = model?.country?.deaths.toString()
                            recoveredCasesNumber.text = model?.country?.recovered.toString()

                            country = model?.country!!
                        }
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        })


    }
}
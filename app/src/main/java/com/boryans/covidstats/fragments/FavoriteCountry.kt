package com.boryans.covidstats.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.activities.MainActivity
import com.boryans.covidstats.adapters.CountryListRecyclerAdapter
import com.boryans.covidstats.listeners.CountryClickListener
import com.boryans.covidstats.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_favorite_country.*


class FavoriteCountry : Fragment(R.layout.fragment_favorite_country), CountryClickListener {



    lateinit var mainViewModel: MainViewModel
    lateinit var favoritesAdapter: CountryListRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = (activity as MainActivity).mainViewModel
        setUpRecyclerView()

        mainViewModel.getFavoriteCountries().observe(viewLifecycleOwner, { country ->
            val countryNames = ArrayList<String>()
            country?.let {
                for (name in country) {
                    countryNames.add(name.country)
                }
            }
            favoritesAdapter.updateCountriesList(countryNames)
        })
    }

    private fun setUpRecyclerView() {
        favoritesAdapter = CountryListRecyclerAdapter(this)
        favoriteCountryRecycler.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCountryClick(countryName: String) {
       Navigation.findNavController(requireView()).navigate(FavoriteCountryDirections.actionFavoriteCountryToCountryDetailsStatsFragment(countryName))
    }

}
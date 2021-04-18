package com.boryans.covidstats.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.Navigation
import com.boryans.covidstats.R
import com.boryans.covidstats.activities.MainActivity
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants.Companion.CHANNEL_ID
import com.boryans.covidstats.util.Constants.Companion.CHANNEL_NAME
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(R.layout.fragment_home) {


    private val random = Random()
    private var NOTIFICATION_ID = 0
    private lateinit var mainViewModel: MainViewModel

    private var listOfCountriesFromLocal = ArrayList<Country>()
    private var listOfCountriesFromRemote = ArrayList<Country>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.setSupportActionBar(my_toolbar)
        setHasOptionsMenu(true)
        mainViewModel = (activity as MainActivity).mainViewModel
        createNotificationChannel()


        my_toolbar.inflateMenu(R.menu.home_toolbar_menu)
        my_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.listOfInfectedCountries -> Navigation.findNavController(requireView())
                    .navigate(HomeFragmentDirections.actionHomeFragmentToCountryList())
                R.id.myCountries -> Navigation.findNavController((requireView()))
                    .navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteCountry())
            }
            true
        }

        mainViewModel.refreshUI()



        mainViewModel.getFavoriteCountries().observe(viewLifecycleOwner, { countries ->
            for (country in countries) {
                listOfCountriesFromLocal.add(country)
            }
        })



        mainViewModel.listOfAllCountries.observe(
            viewLifecycleOwner,
            { listOfCountries ->
                when (listOfCountries) {
                    is Resource.Success -> {
                        hideProgressBar()
                        listOfCountries.let { list ->
                            list.data?.let { countries ->
                                for (country in countries) {
                                    listOfCountriesFromRemote.add(country)
                                }
                                notificationAlert()
                            }
                        }
                    }
                }
            })

        mainViewModel.countryDetails.observe(viewLifecycleOwner, { countryDetails ->
            when (countryDetails) {
                is Resource.Success -> {
                    hideProgressBar()
                    countryDetails.let { details ->
                        setStatsVisibilityToVisible()
                        appendDataToViews(details)
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


    private fun notificationAlert() {
        for (i in 0 until listOfCountriesFromLocal.size) {
            val countryFromLocal = listOfCountriesFromLocal[i]

            for (j in 0 until listOfCountriesFromRemote.size) {
                if (countryFromLocal.country == listOfCountriesFromRemote[j].country) {

                    val deathsDifference = 105 //(listOfCountriesFromRemote[j].deaths - countryFromLocal.deaths)
                    val confirmedCasesDifference = 3001 //(listOfCountriesFromRemote[j].confirmed - countryFromLocal.confirmed)
                    if (deathsDifference > 100 && confirmedCasesDifference > 3000) {
                        pushNotification(deathsDifference, confirmedCasesDifference, countryFromLocal.country!! )
                    }


                }
            }

        }
    }


    private fun pushNotification(deaths: Int, confirmedCases: Int, country: String) {

            NOTIFICATION_ID = random.nextInt(9999-1000) + 1000

            val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setContentTitle("Covid Alert")
                .setSmallIcon(R.drawable.coronavirus)
                .setContentText("Alert for ${country}. Deaths increased by: $deaths and new active cases increased by: $confirmedCases")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val notificationManager = NotificationManagerCompat.from(requireContext())
            notificationManager.notify(NOTIFICATION_ID, notification)



    }

    private fun appendDataToViews(details: Resource.Success<Model>) {
        statsTextView.text = details.data?.country?.country
        totalCases.text = "Total cases: ${details.data?.country?.confirmed}"
        recoveredCases.text = "Recovered cases: ${details.data?.country?.recovered.toString()}"
        deaths.text = "Deaths: ${details.data?.country?.deaths?.toString()}"
        lifeExpectancy.text = "Life expectancy: ${details.data?.country?.lifeExpectancy} years"
        lastUpdated.text = "Last updated: ${details.data?.country?.updated ?: "X"}"


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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

}
package com.boryans.covidstats.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import com.boryans.covidstats.R
import com.boryans.covidstats.model.Country
import com.boryans.covidstats.model.Model
import com.boryans.covidstats.util.Constants.Companion.CHANNEL_ID
import com.boryans.covidstats.util.Constants.Companion.CHANNEL_NAME
import com.boryans.covidstats.util.Constants.Companion.TAG
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList


class HomeFragment : Fragment(R.layout.fragment_home), Toolbar.OnMenuItemClickListener {


    private var NOTIFICATION_ID = 0
    private lateinit var mainViewModel: MainViewModel

    private var listOfFavoriteCountriesFromLocal = ArrayList<Country>()
    private var listOfCountriesFromRemote = ArrayList<Country>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)?.setSupportActionBar(my_toolbar)
        setHasOptionsMenu(true)
        mainViewModel = (activity as MainActivity).mainViewModel
        setToolbar()
        createNotificationChannel()

        getFavoriteCountries()
        observeListOfCountries()
        observeCountryDetails()

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




    private fun setToolbar() {
        my_toolbar.inflateMenu(R.menu.home_toolbar_menu)
        my_toolbar.setOnMenuItemClickListener(this)
    }

    private fun observeCountryDetails() {
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
    }

    private fun observeListOfCountries() {
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
    }

    private fun getFavoriteCountries() {
        mainViewModel.refreshUI()
        mainViewModel.getFavoriteCountries().observe(viewLifecycleOwner, { countries ->
            for (country in countries) {
                listOfFavoriteCountriesFromLocal.add(country) //assign
            }
        })
    }

    private fun notificationAlert() {
        for (i in 0 until listOfFavoriteCountriesFromLocal.size) {
            val countryFromLocal = listOfFavoriteCountriesFromLocal[i]

            for (j in 0 until listOfCountriesFromRemote.size) {
                if (countryFromLocal.country == listOfCountriesFromRemote[j].country) {

                    val deathsDifference =
                        101 //    (listOfCountriesFromRemote[j].deaths - countryFromLocal.deaths)
                    val confirmedCasesDifference =
                        3001 //    (listOfCountriesFromRemote[j].confirmed - countryFromLocal.confirmed)
                    if (deathsDifference > 100 && confirmedCasesDifference > 3000) {
                        pushNotification(
                            deathsDifference,
                            confirmedCasesDifference,
                            countryFromLocal.country!!
                        )
                    }
                }
            }
        }
    }

    private fun pushNotification(deaths: Int, confirmedCases: Int, country: String) {

        CoroutineScope(Dispatchers.IO).launch {
            if (listOfFavoriteCountriesFromLocal.size != 0) {
                NOTIFICATION_ID = mainViewModel.getCountryIdFromDb(country)
            }
        }

        val args = Bundle().apply {
            putString("countryName", country)
        }


        try {

            val pendingIntent = NavDeepLinkBuilder(requireContext())
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.countryDetailsStatsFragment)
                .setArguments(args)
                .createPendingIntent()


            val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setContentTitle(country)
                .setSmallIcon(R.drawable.coronavirus)
                .setContentText("Alert for ${country}. Deaths increased by: $deaths and new active cases increased by: $confirmedCases")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()


            val notificationManager = NotificationManagerCompat.from(requireContext())
            notificationManager.notify(NOTIFICATION_ID, notification)

        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(TAG, "$e")
        }


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
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.RED
                enableLights(true)
            }
            val manager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        item?.let {
            when (it.itemId) {
                R.id.listOfInfectedCountries -> Navigation.findNavController(requireView())
                    .navigate(HomeFragmentDirections.actionHomeFragmentToCountryList())
                R.id.myCountries -> Navigation.findNavController((requireView()))
                    .navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteCountry())
            }
        }
        return true
    }
}
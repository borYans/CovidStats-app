package com.boryans.covidstats.ui

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.boryans.covidstats.R
import com.boryans.covidstats.adapters.CountryListRecyclerAdapter
import com.boryans.covidstats.listeners.CountryClickListener
import com.boryans.covidstats.util.Constants.Companion.TAG
import com.boryans.covidstats.util.Resource
import com.boryans.covidstats.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.details_country_dialog.*
import kotlinx.android.synthetic.main.fragment_country_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.w3c.dom.Text


class CountryList : Fragment(R.layout.fragment_country_list), CountryClickListener {

    private lateinit var countryRecyclerAdapter: CountryListRecyclerAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        Log.d(TAG, "Fragment started!")


        mainViewModel.getListOfAllCountries()
        mainViewModel.listOfAllCountries.observe(viewLifecycleOwner, { listOfCountries ->
            when (listOfCountries) {
                is Resource.Success -> {
                    hideProgressBar()
                    listOfCountries.let { list ->
                        list.data?.let { countries ->
                            countryRecyclerAdapter.updateCountriesList(countries)
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

    private fun setUpRecyclerView() {

        countryRecyclerView.apply {
            countryRecyclerAdapter = CountryListRecyclerAdapter(this@CountryList)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = countryRecyclerAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCountryClick(countryName: String) {
        val countryDetailsDialogInfo = layoutInflater.inflate(R.layout.details_country_dialog, null)
        val loadingDialog: ProgressBar = countryDetailsDialogInfo.findViewById(R.id.loadingDialog)
        val dataLayout: View = countryDetailsDialogInfo.findViewById(R.id.dialogDataLayout)



        val countryNameTxt: TextView = countryDetailsDialogInfo.findViewById(R.id.countryTxtDialog)
        val confirmedCases: TextView = countryDetailsDialogInfo.findViewById(R.id.confirmedTxtdialog)
        val recoveredCases: TextView = countryDetailsDialogInfo.findViewById(R.id.recoveredTxtDialog)
        val deaths: TextView = countryDetailsDialogInfo.findViewById(R.id.deathsTxtDialog)
        val lifeExpected: TextView = countryDetailsDialogInfo.findViewById(R.id.lifeExpectedTxtDialog)
        val lastUpdated: TextView = countryDetailsDialogInfo.findViewById(R.id.lastUpdateTxtDialog)



        mainViewModel.getCountryDetailsData(countryName)
        mainViewModel.countryDetails.observe(viewLifecycleOwner, { countryDetails ->
            when (countryDetails) {
                is Resource.Success -> {
                    dataLayout.visibility = View.VISIBLE
                    loadingDialog.visibility = View.INVISIBLE
                    hideProgressBar()
                    countryDetails.let { details ->
                        details.data.let { country ->

                            countryNameTxt.text = "Country name: ${country?.all?.country ?: "unavailable data."}"
                            confirmedCases.text = "Confirmed cases: ${country?.all?.confirmed?.toString() ?: "unavailable data."}"
                            recoveredCases.text = "Recovered cases: ${country?.all?.recovered?.toString() ?: "unavailable data."}"
                            deaths.text = "Deaths: ${country?.all?.deaths?.toString()?: "unavailable data."}"
                            lifeExpected.text = "Life expectancy: ${country?.all?.lifeExpectancy?: "unavailable data."}"
                            lastUpdated.text = "Updated: ${country?.all?.updated?: "unavailable data."}"


                        }
                    }
                }
                is Resource.Error -> {
                    loadingDialog.visibility = View.VISIBLE
                    dataLayout.visibility = View.GONE
                }

                is Resource.Loading -> {
                   loadingDialog.visibility = View.VISIBLE
                    dataLayout.visibility = View.GONE
                }
            }
        })


        AlertDialog.Builder(requireContext()).apply {
            setView(countryDetailsDialogInfo)
                .setCancelable(true)
                .create()
                .show()
        }
    }



    private fun showProgressBar() {
        recyclerProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        recyclerProgressBar.visibility = View.INVISIBLE
    }






}
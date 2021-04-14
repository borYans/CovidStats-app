package com.boryans.covidstats.activities

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI
import com.boryans.covidstats.R
import com.boryans.covidstats.db.CountryDatabase
import com.boryans.covidstats.repo.CovidStatsRepository
import com.boryans.covidstats.viewmodels.MainViewModel
import com.boryans.covidstats.viewmodels.MainViewModelProviderFactory


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

     lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = CovidStatsRepository(CountryDatabase(this))
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val viewModelProviderFactory = MainViewModelProviderFactory(repository, connectivityManager)
        mainViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

}
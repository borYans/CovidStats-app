package com.boryans.covidstats.activities

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
        val viewModelProviderFactory = MainViewModelProviderFactory(repository)
        mainViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

}
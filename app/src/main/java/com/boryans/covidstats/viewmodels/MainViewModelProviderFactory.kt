package com.boryans.covidstats.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boryans.covidstats.repo.CovidStatsRepository
import com.boryans.covidstats.util.CountryApplication

@Suppress("UNCHECKED_CAST")
class MainViewModelProviderFactory(
    private val repository: CovidStatsRepository,
    private val app: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository, app) as T
    }


}
package com.boryans.covidstats.viewmodels

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boryans.covidstats.repo.CovidStatsRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelProviderFactory(
    private val repository: CovidStatsRepository,
    private val connectivityManager: ConnectivityManager
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository, connectivityManager) as T
    }


}
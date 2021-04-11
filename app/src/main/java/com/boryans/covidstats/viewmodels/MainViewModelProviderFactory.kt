package com.boryans.covidstats.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boryans.covidstats.repo.CovidStatsRepository

class MainViewModelProviderFactory(
    val repository: CovidStatsRepository,
    val app: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(app, repository) as T
    }

}
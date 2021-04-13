package com.boryans.covidstats.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boryans.covidstats.repo.CovidStatsRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelProviderFactory(
    private val repository: CovidStatsRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}
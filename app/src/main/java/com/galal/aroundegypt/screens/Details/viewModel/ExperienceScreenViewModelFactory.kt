package com.galal.aroundegypt.screens.Details.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.galal.aroundegypt.data.repository.HomeRepositoryImpl
import com.galal.aroundegypt.screens.Home.viewModel.HomeViewModel

class ExperienceScreenViewModelFactory(private val repositoryImpl: HomeRepositoryImpl): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExperienceScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExperienceScreenViewModel(repositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
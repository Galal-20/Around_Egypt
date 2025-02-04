package com.galal.aroundegypt.screens.Home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.data.repository.HomeRepositoryImpl
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepositoryImpl): ViewModel() {

    private val _experiences = MutableStateFlow<ApiState<RecommendedExperiences>>(ApiState.Loading)
    val experiences: StateFlow<ApiState<RecommendedExperiences>> = _experiences

    private val _mostRecentExperiences = MutableStateFlow<ApiState<MostRecentExperiences>>(ApiState.Loading)
    val mostRecentExperiences: StateFlow<ApiState<MostRecentExperiences>> = _mostRecentExperiences
    init {
        viewModelScope.launch {
            fetchExperiences()
            fetchMostRecentExperiences()
        }
    }

    private suspend fun fetchExperiences() {
        val response = repository.fetchRecommendedExperiences()
        if (response is ApiState.Success) {
            _experiences.value = ApiState.Success(response.data)
        } else if (response is ApiState.Failure) {
            _experiences.value = ApiState.Failure(response.message)
        }
    }

    private suspend fun fetchMostRecentExperiences() {
        val response = repository.fetchMostRecentExperiences()
        if (response is ApiState.Success) {
            _mostRecentExperiences.value = ApiState.Success(response.data)
        }else if (response is ApiState.Failure){
            _mostRecentExperiences.value = ApiState.Failure(response.message)
        }
    }

}
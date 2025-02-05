package com.galal.aroundegypt.screens.Home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.data.repository.HomeRepositoryImpl
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepositoryImpl): ViewModel() {

    private val _experiences = MutableStateFlow<ApiState<RecommendedExperiences>>(ApiState.Loading)
    val experiences: StateFlow<ApiState<RecommendedExperiences>> = _experiences
    private val _mostRecentExperiences = MutableStateFlow<ApiState<MostRecentExperiences>>(ApiState.Loading)
    val mostRecentExperiences: StateFlow<ApiState<MostRecentExperiences>> = _mostRecentExperiences

    private val _searchResults = MutableStateFlow<ApiState<MostRecentExperiences>>(ApiState.Loading)
    val searchResults: StateFlow<ApiState<MostRecentExperiences>> = _searchResults

    val likedExperiences = mutableSetOf<String>()
    val likedExperiencesMost = mutableSetOf<String>()



    init {
        viewModelScope.launch {
            fetchExperiences()
            fetchMostRecentExperiences()
        }
    }

     suspend fun fetchExperiences() {
        val response = repository.fetchRecommendedExperiences()
        if (response is ApiState.Success) {
            _experiences.value = ApiState.Success(
                response.data.copy(data = response.data.data.map { experiences ->
                    experiences.copy(is_liked = likedExperiences.contains(experiences.id))

                })
            )
        } else if (response is ApiState.Failure) {
            _experiences.value = ApiState.Failure(response.message)
        }
    }

     suspend fun fetchMostRecentExperiences() {
        val response = repository.fetchMostRecentExperiences()
        if (response is ApiState.Success) {
            _mostRecentExperiences.value = ApiState.Success(
                response.data.copy(data = response.data.data.map { experiencesMost ->
                    experiencesMost.copy(is_liked = likedExperiencesMost.contains(experiencesMost.id),
                        era = experiencesMost.era
                        )
                })
            )
        }else if (response is ApiState.Failure){
            _mostRecentExperiences.value = ApiState.Failure(response.message)
        }
    }



    fun likeExperience(id: String) {
        viewModelScope.launch {
            val result = repository.likeExperience(id)
            if (result is ApiState.Success) {
                likedExperiences.add(id)
                Log.d("LIKE_SUCCESS", "Experience Liked")

                _experiences.update { currentState ->
                    if (currentState is ApiState.Success) {
                        val updatedList = currentState.data.data.map { experience ->
                            if (experience.id == id) {
                                experience.copy(
                                    is_liked = true,
                                    likes_no = (experience.likes_no ?: 0) + 1,
                                    era = experience.era
                                )
                            } else experience
                        }
                        ApiState.Success(currentState.data.copy(data = updatedList))
                    } else currentState
                }
                //delay(1500)

                //  get fresh data after like.
                val newResponse = repository.fetchRecommendedExperiences()
                if (newResponse is ApiState.Success) {
                    Log.d("API_RESPONSE", "Received experiences: ${newResponse.data}")

                    _experiences.update { currentState ->
                        if (currentState is ApiState.Success) {
                            val updatedList = newResponse.data.data.map { newExperience ->
                                val previousExperience = currentState.data.data.find { it.id == newExperience.id }
                                newExperience.copy(
                                    is_liked = previousExperience?.is_liked ?: newExperience.is_liked,
                                    era = newExperience.era
                                )
                            }
                            ApiState.Success(newResponse.data.copy(data = updatedList))
                        } else {
                            newResponse
                        }
                    }
                }

                //fetchExperiences()
                fetchMostRecentExperiences()
            } else {
                Log.e("LIKE_ERROR", "Failed to like experience")
            }
        }
    }


    fun searchExperiences(title: String) {
        viewModelScope.launch {
            _searchResults.value = ApiState.Loading
            val response = repository.searchExperiences(title)
            _searchResults.value = response
        }
    }



}

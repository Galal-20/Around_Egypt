package com.galal.aroundegypt.screens.Details.viewModel

import androidx.lifecycle.ViewModel
import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.data.repository.HomeRepositoryImpl
import com.galal.aroundegypt.model.Details.ExperiencesDetails
import kotlinx.coroutines.flow.MutableStateFlow

class ExperienceScreenViewModel(private val repository: HomeRepositoryImpl):ViewModel() {


    private val _experienceDetails = MutableStateFlow<ApiState<ExperiencesDetails>>(ApiState.Loading)
    val experienceDetails: MutableStateFlow<ApiState<ExperiencesDetails>> = _experienceDetails

    suspend fun fetchExperienceDetails(id: String) {
        val response = repository.fetchExperienceDetailsForRecommended(id)
        _experienceDetails.value = response
    }


}





/* private val _experienceDetails = MutableStateFlow<ApiState<Data>>(ApiState.Loading)
   val experienceDetails: MutableStateFlow<ApiState<Data>> = _experienceDetails*/

/*suspend fun fetchRecommendedExperiencesByID(id: String) {
    val response = repository.fetchExperienceDetailsForRecommended(id)
    if (response is ApiState.Success) {
        _experienceDetails.value = ApiState.Success(response.data)
    }else if (response is ApiState.Failure){
        _experienceDetails.value = ApiState.Failure(response.message)
    }
}*/
/*private val _recommendedExperiences = MutableStateFlow<ApiState<RecommendedExperiences>>(ApiState.Loading)
    val recommended: MutableStateFlow<ApiState<RecommendedExperiences>> = _recommendedExperiences



     suspend fun fetchRecommendedExperiencesByID(id: String) {
        val response = repository.fetchExperienceDetailsForRecommended(id)
        if (response is ApiState.Success) {
            _recommendedExperiences.value = ApiState.Success(response.data)
        }else if (response is ApiState.Failure){
            _recommendedExperiences.value = ApiState.Failure(response.message)
        }
    }*/
/*  private val _recommended = MutableStateFlow<ApiState<RecommendedExperiences>>(ApiState.Loading)
   val recommended: MutableStateFlow<ApiState<RecommendedExperiences>> = _recommended

   private val _mostRecent = MutableStateFlow<ApiState<MostRecentExperiences>>(ApiState.Loading)
   val mostRecent: MutableStateFlow<ApiState<MostRecentExperiences>> = _mostRecent

   fun fetchRecommendedExperiencesByID(id: String) {
       viewModelScope.launch {
           _recommended.value = repository.fetchExperienceDetailsForRecommended(id)
       }
   }

   fun fetchMostRecentExperiencesByID(id: String) {
       viewModelScope.launch {
           _mostRecent.value = repository.fetchExperienceDetailsForMost(id)
       }
   }*/
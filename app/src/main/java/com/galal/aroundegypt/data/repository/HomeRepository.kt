package com.galal.aroundegypt.data.repository

import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.model.Details.ExperiencesDetails
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences

interface HomeRepository {
    suspend fun fetchRecommendedExperiences():ApiState<RecommendedExperiences>
    suspend fun fetchMostRecentExperiences():ApiState<MostRecentExperiences>
    suspend fun fetchExperienceDetailsForRecommended(id: String):ApiState<ExperiencesDetails>
}
package com.galal.aroundegypt.data.api

import com.galal.aroundegypt.model.Details.ExperiencesDetails
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AroundApi {
    @GET("experiences?")
    suspend fun getRecommendedExperiences(
        @Query("filter[recommended]") recommended: Boolean = true
    ): RecommendedExperiences

    @GET("experiences#")
    suspend fun getMostRecentExperiences():MostRecentExperiences


    @GET("experiences/{id}")
    suspend fun getExperienceDetailsForRecommended(@Path("id") id: String): ExperiencesDetails

}



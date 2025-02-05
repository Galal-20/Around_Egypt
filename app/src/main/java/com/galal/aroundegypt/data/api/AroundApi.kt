package com.galal.aroundegypt.data.api

import com.galal.aroundegypt.model.Details.ExperiencesDetails
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AroundApi {
    @GET("experiences?")
    suspend fun getRecommendedExperiences(
        @Query("filter[recommended]") recommended: Boolean = true,
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): RecommendedExperiences

    @GET("experiences")
    suspend fun getMostRecentExperiences(
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ):MostRecentExperiences


    @GET("experiences/{id}")
    suspend fun getExperienceDetailsForRecommended(@Path("id") id: String): ExperiencesDetails


    @POST("experiences/{id}/like")
    suspend fun likeExperience(@Path("id") id: String): retrofit2.Response<Unit>
}



package com.galal.aroundegypt.data.repository

import android.util.Log
import com.galal.aroundegypt.data.api.ApiState
import com.galal.aroundegypt.data.api.AroundApi
import com.galal.aroundegypt.model.Details.ExperiencesDetails
import com.galal.aroundegypt.model.Most.MostRecentExperiences
import com.galal.aroundegypt.model.Recommanded.RecommendedExperiences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HomeRepositoryImpl(private val apiService: AroundApi) : HomeRepository {
    override suspend fun fetchRecommendedExperiences(): ApiState<RecommendedExperiences> = withContext(
        Dispatchers.IO)  {
        return@withContext try {
            val response = apiService.getRecommendedExperiences()
            ApiState.Success(response)
        }catch (e: Exception){
            ApiState.Failure(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun fetchMostRecentExperiences(): ApiState<MostRecentExperiences> = withContext(
        Dispatchers.IO
    ) {
        return@withContext try {
            val response = apiService.getMostRecentExperiences()
            ApiState.Success(response)
        }catch (e: Exception){
            ApiState.Failure(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun fetchExperienceDetailsForRecommended(id: String): ApiState<ExperiencesDetails> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val response = apiService.getExperienceDetailsForRecommended(id)

                // Log response for debugging
                Log.d("API_RESPONSE", "Received: $response")

                if (response != null) {
                    ApiState.Success(response) // Return object directly
                } else {
                    ApiState.Failure("Experience not found")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching experience: ${e.localizedMessage}")
                ApiState.Failure(e.localizedMessage ?: "Unknown Error")
            }
        }

}

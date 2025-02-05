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
                Log.d("API_RESPONSE", "Received: $response")

                if (response != null) {
                    ApiState.Success(response) // return object
                } else {
                    ApiState.Failure("Experience not found")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching experience: ${e.localizedMessage}")
                ApiState.Failure(e.localizedMessage ?: "Unknown Error")
            }
        }



    suspend fun likeExperience(id: String): ApiState<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d("LIKE_REQUEST", "Liking experience with ID: $id")
            val response = apiService.likeExperience(id)
            Log.d("API_RESPONSE", "Like Response Code: ${response.code()}")

            val updatedDetails = apiService.getExperienceDetailsForRecommended(id)
            Log.d("API_RESPONSE", "Updated Experience Details: $updatedDetails")

            if (response.isSuccessful) {
                Log.d("API_SUCCESS", "Experience Liked Successfully")
                ApiState.Success(Unit)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                Log.e("API_ERROR", "Failed to Like Experience: $errorMessage")
                ApiState.Failure("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Exception: ${e.localizedMessage}")
            ApiState.Failure(e.localizedMessage ?: "Unknown Error")
        }
    }
}


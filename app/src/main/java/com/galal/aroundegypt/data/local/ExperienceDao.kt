package com.galal.aroundegypt.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExperienceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendedExperience(experience: List<RecommendedExperience>)



    @Query("SELECT * FROM recommended_experiences")
    suspend fun getRecommendedExperiences(): List<RecommendedExperience>


    @Query("DELETE FROM recommended_experiences")
    suspend fun clearRecommendedExperiences()


}


/*@Query("SELECT * FROM most_recent_experiences")
suspend fun getMostRecentExperiences(): List<MostRecentExperience>
*/
/*  @Query("DELETE FROM most_recent_experiences")
  suspend fun clearMostRecentExperiences()*/
/*@Insert(onConflict = OnConflictStrategy.REPLACE)
 suspend fun insertMostRecentExperience(experience: MostRecentExperience)*/
package com.galal.aroundegypt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [RecommendedExperience::class], version = 2)
abstract class ExperienceDatabase: RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao

    companion object{
        @Volatile
        private var INSTANCE: ExperienceDatabase? = null

        fun getInstance(context: Context): ExperienceDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExperienceDatabase::class.java,
                    "experience_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

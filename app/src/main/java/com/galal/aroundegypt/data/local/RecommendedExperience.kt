package com.galal.aroundegypt.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recommended_experiences")
data class RecommendedExperience(
     @PrimaryKey val id: String,
     val title: String,
     val cover_photo: String?,
     val likes_no: Int?,
     val views_no: Int?,
     val city: String?,
     val description: String?,
)
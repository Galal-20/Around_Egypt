package com.galal.aroundegypt.model.Most

data class Review(
    val comment: String,
    val created_at: String,
    val experience: String,
    val id: String,
    val name: String,
    val rating: Int
)
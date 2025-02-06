package com.galal.aroundegypt.model.Recommanded

import kotlinx.serialization.Serializable

data class Period(
    val created_at: String,
    val id: String,
    val updated_at: String,
    val value: String
)
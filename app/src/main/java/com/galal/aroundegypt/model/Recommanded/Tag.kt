package com.galal.aroundegypt.model.Recommanded

import kotlinx.serialization.Serializable

data class Tag(
   val disable: Any,
    val id: Int,
    val name: String,
    val top_pick: Int
)
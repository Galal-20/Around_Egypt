package com.galal.aroundegypt.model.Details

import kotlinx.serialization.Serializable


data class ExperiencesDetails(
    val `data`: Data,
    val meta: Meta,
    val pagination: Pagination
)
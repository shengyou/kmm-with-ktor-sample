package io.kraftsman.responses

import kotlinx.serialization.Serializable

@Serializable
class NewsResponse(
    val id: Int,
    val title: String,
    val summary: String,
    val date: String,
    val imageUrl: String,
    val content: String,
    val editor: String,
)

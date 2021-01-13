package io.kraftsman.requests

data class NewsRequest(
    val id: Int,
    val title: String,
    val summary: String,
    val date: String,
    val imageUrl: String,
)

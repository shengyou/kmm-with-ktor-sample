package io.kraftsman.requests

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NewsRequest(
    val id: Int,
    val title: String,
    val summary: String,
    val date: String,
    val imageUrl: String,
)

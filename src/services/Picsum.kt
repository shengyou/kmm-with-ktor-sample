package io.kraftsman.services

/**
 * Get image from Picsum service
 * https://picsum.photos/
 */
class Picsum {
    val endpoint = "https://picsum.photos"

    fun getImage(width: Int = 200, height: Int = 300): String {
        return "$endpoint/$width/$height"
    }
}

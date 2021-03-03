package io.kraftsman.extensions

import io.ktor.application.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val Application.publicUrl get() = environment.config.property("ktor.asset.url").getString()

fun LocalDateTime.toDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return this.format(formatter)
}

fun LocalDateTime.toDateTimeString(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return this.format(formatter)
}

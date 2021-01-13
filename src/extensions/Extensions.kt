package io.kraftsman.extensions

import io.ktor.application.*
import org.joda.time.DateTime

val Application.publicUrl get() = environment.config.property("ktor.asset.url").getString()

fun DateTime.toDateString(): String {
    return this.toString("yyyy-MM-dd")
}

fun DateTime.toDateTimeString(): String {
    return this.toString("yyyy-MM-dd HH:mm:ss")
}

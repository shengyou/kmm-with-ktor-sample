package io.kraftsman.extensions

import org.joda.time.DateTime

fun DateTime.toDateString(): String {
    return this.toString("yyyy-MM-dd")
}

fun DateTime.toDateTimeString(): String {
    return this.toString("yyyy-MM-dd HH:mm:ss")
}

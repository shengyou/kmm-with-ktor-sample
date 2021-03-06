package io.kraftsman.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.`java-time`.datetime

object News: IntIdTable() {
    val title = varchar("title", 255)
    val summary = varchar("summary", 500)
    val date = datetime("date")
    val imageUrl = varchar("imageUrl", 500)
    val content = text("content")
    val editor = varchar("editor", 255)
}

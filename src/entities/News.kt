package io.kraftsman.entities

import io.kraftsman.tables.News as NewsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class News(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<News>(NewsTable)

    var title by NewsTable.title
    var summary by NewsTable.summary
    var date by NewsTable.date
    var imageUrl by NewsTable.imageUrl
    var content by NewsTable.content
    var editor by NewsTable.editor
}

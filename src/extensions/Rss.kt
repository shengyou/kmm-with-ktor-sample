package io.kraftsman.extensions

import tw.ktrssreader.annotation.RssRawData
import tw.ktrssreader.annotation.RssTag

@RssTag(name = "channel")
data class JetBrainsCustomChannel(
    val title: String?,
    val description: String?,
    @RssTag(name = "item")
    val items: List<JetBrainsCustomItem>,
)

@RssTag(name = "item")
data class JetBrainsCustomItem(
    val title: String?,
    @RssRawData(rawTags = ["dc:creator"])
    val author: String?,
    val pubDate: String?,
    val description: String?,
    val link: String?,
)

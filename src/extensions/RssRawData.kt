package io.kraftsman.extensions

import tw.ktrssreader.annotation.RssRawData
import tw.ktrssreader.annotation.RssTag
import java.io.Serializable

@RssTag(name = "item")
data class RawRssData(
    @RssRawData(rawTags = ["dc:creator"])
    val author: String?,
): Serializable

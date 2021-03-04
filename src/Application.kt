package io.kraftsman

import io.kraftsman.entities.News
import io.kraftsman.extensions.publicUrl
import io.kraftsman.extensions.toDateString
import io.kraftsman.requests.NewsRequest
import io.kraftsman.responses.NewsResponse
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tw.ktrssreader.kotlin.parser.RssStandardParser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import io.kraftsman.tables.News as NewsTable

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }

    Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        driver = "org.h2.Driver"
    )

    transaction {
        SchemaUtils.create(NewsTable)
    }

    val client = OkHttpClient()
    val request = Request.Builder().url("https://blog.jetbrains.com/kotlin/feed/").build()
    val xmlString = client.newCall(request).execute().body?.string() ?: ""
    val rssParser = RssStandardParser().parse(xmlString)

    transaction {
        rssParser.items?.forEach {
            val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
            val datetime = LocalDateTime.parse(it.pubDate, formatter)

            println("Author: ${it.author.toString()}")

            News.new {
                title = it.title.toString()
                summary = it.description?.substring(0..150)+"..."
                date = datetime
                //imageUrl = Picsum().getImage()
                imageUrl = "$publicUrl/${Random.nextInt(1, 30)}.jpeg"
                content = it.description.toString()
                editor = it.author.toString()
            }
        }
    }

    routing {

        static("static") {
            resources("static")
        }

        get("/") {
            call.respond(mapOf("message" to "Hello, Ktor"))
        }

        get("/news") {
            val newsList = transaction {
                News.all()
                    .sortedByDescending { it.id }
                    .map {
                        NewsResponse(
                            id = it.id.value,
                            title = it.title,
                            summary = it.summary,
                            date = it.date.toDateString(),
                            imageUrl = it.imageUrl,
                            content = it.content,
                            editor = it.editor,
                        )
                    }
            }

            call.respond(newsList)
        }

        post("/news") {
            val newsRequest = call.receive<NewsRequest>()
            val newsResponse = transaction {
                News.findById(newsRequest.id)?.let {
                    NewsResponse(
                        id = it.id.value,
                        title = it.title,
                        summary = it.summary,
                        date = it.date.toDateString(),
                        imageUrl = it.imageUrl,
                        content = it.content,
                        editor = it.editor,
                    )
                }
            }

            if (newsResponse != null) {
                call.respond(newsResponse)
            } else {
                call.respond(HttpStatusCode.NotFound, "Not found")
            }
        }
    }
}

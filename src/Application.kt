package io.kraftsman

import com.github.javafaker.Faker
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
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*
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

    transaction {
        val faker = Faker(Locale("zh-CN"))

        for (i in 1..10) {
            News.new {
                title = faker.lorem().sentence()
                summary = faker.lorem().paragraph(1)
                date = DateTime.now()
                //imageUrl = Picsum().getImage()
                imageUrl = "$publicUrl/${Random.nextInt(1, 30)}.jpeg"
                content = faker.lorem().paragraph(3)
                editor = faker.name().fullName()
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
            val request = call.receive<NewsRequest>()
            val newsResponse = transaction {
                News.findById(request.id)?.let {
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

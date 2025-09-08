package com.fedorovsky.mkdservice.app.ktor

import com.fedorovsky.mkdservice.api.v1.apiV1Mapper
import com.fedorovsky.mkdservice.app.ktor.plugins.initAppSettings
import com.fedorovsky.mkdservice.app.ktor.v1.v1Meter
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.host
import io.ktor.server.application.install
import io.ktor.server.application.port
import io.ktor.server.cio.CIO
import io.ktor.server.config.yaml.YamlConfigLoader
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main(){
    embeddedServer(CIO, environment = applicationEngineEnvironment {
        val conf = YamlConfigLoader().load("./application.yaml")
            ?: throw RuntimeException("Cannot read application.yaml")
        println(conf)
        config = conf
        println("File read")

        module {
            module()
        }

        connector {
            port = conf.port
            host = conf.host
        }
        println("Starting")
    }).start(true)
}

fun Application.module(
    appSettings: MkdAppSettings = initAppSettings()
) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowCredentials = true
        /* TODO
            Это временное решение, оно опасно.
            В боевом приложении здесь должны быть конкретные настройки
        */
        anyHost()
    }

    routing {
        get("/") {
            call.respondText("Hello, world!")
        }
        route("v1") {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }
            v1Meter(appSettings)
        }
    }
}
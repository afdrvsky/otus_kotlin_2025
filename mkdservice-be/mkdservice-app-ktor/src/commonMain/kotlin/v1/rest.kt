package com.fedorovsky.mkdservice.app.ktor.v1

import com.fedorovsky.mkdservice.app.ktor.MkdAppSettings
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.v1Meter(appSettings: MkdAppSettings) {
    route("meter") {
        post("create") {
            call.createMeterReading(appSettings)
        }
        post("read") {
            call.readMeterReading(appSettings)
        }
        post("update") {
            call.updateMeterReading(appSettings)
        }
        post("delete") {
            call.deleteMeterReading(appSettings)
        }
        options("create") {
            call.respond(HttpStatusCode.OK, mapOf(
                "Allow" to "GET, POST, OPTIONS",
                "Access-Control-Allow-Methods" to "GET, POST, OPTIONS",
                "Access-Control-Allow-Headers" to "Content-Type"
            ))
        }
    }
}
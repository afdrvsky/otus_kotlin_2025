package com.fedorovsky.mkdservice.app.ktor.v1

import com.fedorovsky.mkdservice.app.ktor.MkdAppSettings
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

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
    }
}
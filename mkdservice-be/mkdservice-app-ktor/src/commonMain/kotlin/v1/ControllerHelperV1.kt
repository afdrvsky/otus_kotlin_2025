package com.fedorovsky.mkdservice.app.ktor.v1

import com.fedorovsky.mkdservice.api.v1.mappers.fromTransport
import com.fedorovsky.mkdservice.api.v1.mappers.toTransportMeter
import com.fedorovsky.mkdservice.api.v1.models.IRequest
import com.fedorovsky.mkdservice.api.v1.models.IResponse
import com.fedorovsky.mkdservice.app.common.AUTH_HEADER
import com.fedorovsky.mkdservice.app.common.controllerHelper
import com.fedorovsky.mkdservice.app.common.jwt2principal
import com.fedorovsky.mkdservice.app.ktor.MkdAppSettings
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlin.reflect.KClass

suspend inline fun <reified Q : IRequest, @Suppress("unused") reified R : IResponse> ApplicationCall.processV1(
    appSettings: MkdAppSettings,
    clazz: KClass<*>,
    logId: String,
) = appSettings.controllerHelper(
    {
        principal = this@processV1.request.header(AUTH_HEADER).jwt2principal()
        fromTransport(this@processV1.receive<Q>())
    },
    { this@processV1.respond(toTransportMeter() as R) },
    clazz,
    logId,
)
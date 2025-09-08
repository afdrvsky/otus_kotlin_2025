package com.fedorovsky.mkdservice.app.ktor.v1

import com.fedorovsky.mkdservice.api.v1.models.MeterCreateResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingCreateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateResponse
import com.fedorovsky.mkdservice.app.ktor.MkdAppSettings
import io.ktor.server.application.ApplicationCall
import kotlin.reflect.KClass

val clCreate: KClass<*> = ApplicationCall::createMeterReading::class
suspend fun ApplicationCall.createMeterReading(appSettings: MkdAppSettings) =
    processV1<MeterReadingCreateRequest, MeterCreateResponse>(appSettings, clCreate,"create")

val clRead: KClass<*> = ApplicationCall::readMeterReading::class
suspend fun ApplicationCall.readMeterReading(appSettings: MkdAppSettings) =
    processV1<MeterReadingReadRequest, MeterReadResponse>(appSettings, clRead, "read")

val clUpdate: KClass<*> = ApplicationCall::updateMeterReading::class
suspend fun ApplicationCall.updateMeterReading(appSettings: MkdAppSettings) =
    processV1<MeterUpdateRequest, MeterUpdateResponse>(appSettings, clUpdate, "update")

val clDelete: KClass<*> = ApplicationCall::deleteMeterReading::class
suspend fun ApplicationCall.deleteMeterReading(appSettings: MkdAppSettings) =
    processV1<MeterDeleteRequest, MeterDeleteResponse>(appSettings, clDelete, "delete")
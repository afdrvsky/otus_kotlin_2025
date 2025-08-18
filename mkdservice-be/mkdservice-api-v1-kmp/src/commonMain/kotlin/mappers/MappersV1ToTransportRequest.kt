package com.fedorovsky.mkdservice.api.v1.mappers

import com.fedorovsky.mkdservice.api.v1.models.MeterCreateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteObject
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadObject
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateObject
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading

fun MeterReading.toTransportCreateMeter() = MeterCreateObject(
    amount = amount.asString(),
    unit = unit.toString(),
    meterId = meterId.toTransportMeter(),
    apartmentId = apartmentId.asInt(),
)

fun MeterReading.toTransportReadMeter() = MeterReadingReadObject(
    id = id.asInt(),
    meterId = meterId.toTransportMeter(),
    apartmentId = apartmentId.asInt(),
)

fun MeterReading.toTransportUpdateMeter() = MeterUpdateObject(
    amount = amount.asString(),
    unit = unit.toString(),
    meter = id.asInt(),
)

internal fun MeterId.toTransportMeter() = takeIf { it != MeterId.NONE }?.asInt()


fun MeterReading.toTransportDeleteMeter() = MeterDeleteObject(
    id = id.asInt(),
)
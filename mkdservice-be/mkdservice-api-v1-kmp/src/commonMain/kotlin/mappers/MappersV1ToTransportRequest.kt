package com.fedorovsky.mkdservice.api.v1.mappers

import com.fedorovsky.mkdservice.api.v1.models.MeterCreateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteObject
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadObject
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateObject
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingLock

fun MeterReading.toTransportCreateMeter() = MeterCreateObject(
    amount = amount.asString(),
    unit = unit.toString(),
    meterId = meterId.toTransportMeter(),
    apartmentId = apartmentId.asString(),
)

fun MeterReading.toTransportReadMeter() = MeterReadingReadObject(
    id = id.asString(),
    meterId = meterId.toTransportMeter(),
    apartmentId = apartmentId.asString(),
)

fun MeterReading.toTransportUpdateMeter() = MeterUpdateObject(
    amount = amount.asString(),
    unit = unit.toString(),
    id = id.asString(),
    lock = lock.toTransportMeter(),
)

internal fun MeterId.toTransportMeter() = takeIf { it != MeterId.NONE }?.asString()
internal fun MeterReadingLock.toTransportMeter() = takeIf { it != MeterReadingLock.NONE }?.asString()


fun MeterReading.toTransportDeleteMeter() = MeterDeleteObject(
    id = id.asString(),
    lock = lock.toTransportMeter(),
)
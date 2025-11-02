package com.fedorovsky.mkdservice.api.v1.mappers

import com.fedorovsky.mkdservice.api.v1.models.*
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.exceptions.UnknownMeterCommand
import com.fedorovsky.mkdservice.common.models.*

fun MeterReadingContext.toTransportMeter(): IResponse = when (val cmd = command) {
    MeterCommand.CREATE -> toTransportCreate()
    MeterCommand.READ -> toTransportRead()
    MeterCommand.UPDATE -> toTransportUpdate()
    MeterCommand.DELETE -> toTransportDelete()
    MeterCommand.NONE -> throw UnknownMeterCommand(cmd)
}

fun MeterReadingContext.toTransportCreate() = MeterCreateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meter = meterReadingResponse.toTransportMeter()
)

fun MeterReadingContext.toTransportUpdate() = MeterUpdateResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meter = meterReadingResponse.toTransportMeter()
)

fun MeterReadingContext.toTransportRead() = MeterReadResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meters = metersReadingResponse.toTransportMeter()
)

fun MeterReadingContext.toTransportDelete() = MeterDeleteResponse(
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    meter = meterReadingResponse.toTransportMeter()
)

fun List<MeterReading>.toTransportMeter(): List<MeterResponseObject>? = this
    .map { it.toTransportMeter() }
    .toList()
    .takeIf { it.isNotEmpty() }

fun MeterReading.toTransportMeter(): MeterResponseObject = MeterResponseObject(
    id = id.toTransportMeter(),
    amount = amount.takeIf { it != Amount.NONE }?.asString(),
    unit = unit.takeIf { it != MeterReadingUnit.NONE }?.toString(),
    dateTime = dateTime.takeIf { it.isNotBlank() },
    meterId = meterId.takeIf { it != MeterId.NONE }?.asString(),
    apartmentId = apartmentId.takeIf { it != ApartmentId.NONE }?.asString(),
    lock = lock.takeIf { it != MeterReadingLock.NONE }?.asString()
)

internal fun MeterReadingId.toTransportMeter() = takeIf { it != MeterReadingId.NONE }?.asString()

private fun List<MeterError>.toTransportErrors(): List<com.fedorovsky.mkdservice.api.v1.models.Error>? = this
    .map { it.toTransportMeter() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun MeterError.toTransportMeter() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun MeterReadingState.toResult(): ResponseResult? = when (this) {
    MeterReadingState.RUNNING -> ResponseResult.SUCCESS
    MeterReadingState.FAILING -> ResponseResult.ERROR
    MeterReadingState.FINISHING -> ResponseResult.SUCCESS
    MeterReadingState.NONE -> null
}

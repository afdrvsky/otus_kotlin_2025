package com.fedorovsky.mkdservice.api.v1.mappers

import com.fedorovsky.mkdservice.api.v1.models.*
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.*
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs

fun MeterReadingContext.fromTransport(request: IRequest) = when (request) {
    is MeterReadingCreateRequest -> fromTransport(request)
    is MeterReadingReadRequest -> fromTransport(request)
    is MeterUpdateRequest -> fromTransport(request)
    is MeterDeleteRequest -> fromTransport(request)
}

private fun Int?.toMeterReadingId() = this?.let { MeterReadingId(it) } ?: MeterReadingId.NONE
private fun Int?.toMeterId() = this?.let { MeterId(it) } ?: MeterId.NONE
private fun String?.toAmount() = this?.let { Amount(it) } ?: Amount.NONE
private fun String?.toUnit() = this?.let { MeterReadingUnit.valueOf(it) } ?: MeterReadingUnit.NONE
private fun Int?.toApartmentId() = this?.let { ApartmentId(it) } ?: ApartmentId.NONE

private fun MeterReadingReadObject?.toInternal() = if (this != null) {
    MeterReading(
        id = id.toMeterReadingId(),
        meterId = meterId.toMeterId(),
        apartmentId = apartmentId.toApartmentId()
    )
} else {
    MeterReading()
}

private fun MeterDebug?.transportToWorkMode(): MeterWorkMode = when (this?.mode) {
    MeterRequestDebugMode.PROD -> MeterWorkMode.PROD
    MeterRequestDebugMode.TEST -> MeterWorkMode.TEST
    MeterRequestDebugMode.STUB -> MeterWorkMode.STUB
    null -> MeterWorkMode.PROD
}

private fun MeterDebug?.transportToStubCase(): MeterReadingStubs = when (this?.stub) {
    MeterRequestDebugStubs.SUCCESS -> MeterReadingStubs.SUCCESS
    MeterRequestDebugStubs.NOT_FOUND -> MeterReadingStubs.NOT_FOUND
    MeterRequestDebugStubs.BAD_AMOUNT -> MeterReadingStubs.BAD_AMOUNT
    MeterRequestDebugStubs.BAD_UNIT -> MeterReadingStubs.BAD_UNIT
    MeterRequestDebugStubs.BAD_APARTMENT_ID -> MeterReadingStubs.BAD_APARTMENT_ID
    MeterRequestDebugStubs.BAD_METER_ID -> MeterReadingStubs.BAD_METER_ID
    MeterRequestDebugStubs.CANNOT_DELETE -> MeterReadingStubs.CANNOT_DELETE
    null -> MeterReadingStubs.NONE
}

fun MeterReadingContext.fromTransport(request: MeterReadingCreateRequest) {
    command = MeterCommand.CREATE
    meterReadingRequest = request.meter?.toInternal() ?: MeterReading()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MeterReadingContext.fromTransport(request: MeterReadingReadRequest) {
    command = MeterCommand.READ
    meterReadingRequest = request.meter?.toInternal() ?: MeterReading()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MeterReadingContext.fromTransport(request: MeterUpdateRequest) {
    command = MeterCommand.UPDATE
    meterReadingRequest = request.meter?.toInternal() ?: MeterReading()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun MeterReadingContext.fromTransport(request: MeterDeleteRequest) {
    command = MeterCommand.DELETE
    meterReadingRequest = request.meter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun MeterDeleteObject?.toInternal(): MeterReading = if (this != null) {
    MeterReading(
        id = id.toMeterReadingId(),
    )
} else {
    MeterReading()
}

private fun MeterCreateObject.toInternal(): MeterReading = MeterReading(
    meterId = this.meterId.toMeterId(),
    amount = this.amount.toAmount(),
    unit = this.unit.toUnit(),
    apartmentId = this.apartmentId.toApartmentId(),
)

private fun MeterUpdateObject.toInternal(): MeterReading = MeterReading(
    id = this.meter.toMeterReadingId(),
    amount = this.amount.toAmount(),
    unit = this.unit.toUnit(),
)
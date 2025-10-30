package com.fedorovsky.mkdservice.api.log.mapper

import com.fedorovsky.mkdservice.api.log.models.CommonLogModel
import com.fedorovsky.mkdservice.api.log.models.ErrorLogModel
import com.fedorovsky.mkdservice.api.log.models.MeterLog
import com.fedorovsky.mkdservice.api.log.models.MkdLogModel
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit
import com.fedorovsky.mkdservice.common.models.MeterRequestId
import kotlinx.datetime.Clock

fun MeterReadingContext.toLog(logId: String) = CommonLogModel(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "mkdservice",
    meter = toMeterLog(),
    errors = errors.map { it.toLog() },
)

private fun MeterReadingContext.toMeterLog(): MkdLogModel? {
    val meterNone = MeterReading()
    return MkdLogModel(
        requestId = requestId.takeIf { it != MeterRequestId.NONE }?.asString(),
        requestMeter = meterReadingRequest.takeIf { it != meterNone}?.toLog(),
        responseMeter = meterReadingResponse.takeIf { it != meterNone }?.toLog(),
    ).takeIf { it != meterNone }
}

private fun MeterError.toLog() = ErrorLogModel(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun MeterReading.toLog() = MeterLog(
    id = id.takeIf { it != MeterReadingId.NONE }?.toString(),
    dateTime = dateTime.takeIf { it.isNotBlank() },
    amount = amount.takeIf { it != Amount.NONE }?.toString(),
    unit = unit.takeIf { it != MeterReadingUnit.NONE }?.toString(),
    meterId = meterId.takeIf { it != MeterId.NONE }?.toString(),
    apartmentId = apartmentId.takeIf { it != ApartmentId.NONE }?.toString(),
    permissions = null
)
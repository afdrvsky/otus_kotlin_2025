package com.fedorovsky.mkdservice.stubs

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.stubs.MeterReadingSendStub.METER_READING_SEND

object MeterReadingStub {
    fun get(): MeterReading = METER_READING_SEND.copy()

    fun prepareResult(block: MeterReading.() -> Unit): MeterReading = get().apply(block)

    fun prepareMeterReading() = meterReading(METER_READING_SEND)

    private fun meterReading(base: MeterReading) = base.copy(
        amount = base.amount,
        id = base.id,
        unit = base.unit,
        dateTime = base.dateTime,
        meterId = base.meterId,
        apartmentId = base.apartmentId,
    )
}
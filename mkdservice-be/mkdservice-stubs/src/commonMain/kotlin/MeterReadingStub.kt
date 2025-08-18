package com.fedorovsky.mkdservice.stubs

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.stubs.MeterReadingSendStub.METER_READING_SEND

object MeterReadingStub {
    fun get(): MeterReading = METER_READING_SEND.copy()

    fun prepareResult(block: MeterReading.() -> Unit): MeterReading = get().apply(block)
}
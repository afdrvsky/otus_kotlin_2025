package com.fedorovsky.mkdservice.biz

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.stubs.MeterReadingStub

@Suppress("unused", "RedundantSuspendModifier")
class MkdMeterProcessor(val corSettings: MkdCorSettings) {

    suspend fun exec(ctx: MeterReadingContext) {
        ctx.meterReadingResponse = MeterReadingStub.get()
        ctx.meterReadingResponse = MeterReadingStub.prepareMeterReading()
        ctx.state = MeterReadingState.RUNNING
    }
}
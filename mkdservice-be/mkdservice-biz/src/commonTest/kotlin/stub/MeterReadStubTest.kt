package com.fedorovsky.mkdservice.biz.stub

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import com.fedorovsky.mkdservice.stubs.MeterReadingStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MeterReadStubTest {

    private val processor = MkdMeterProcessor()
    val meterReadingId = MeterReadingId("123")

    @Test
    fun read() = runTest {
        val ctx = MeterReadingContext(
            command = MeterCommand.READ,
            state = MeterReadingState.NONE,
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.SUCCESS,
            meterReadingRequest = MeterReading(
                id = meterReadingId,
            ),
        )
        processor.exec(ctx)
        with (MeterReadingStub.get()) {
            assertEquals(meterReadingId, ctx.meterReadingResponse.id)
            assertEquals(dateTime, ctx.meterReadingResponse.dateTime)
            assertEquals(amount, ctx.meterReadingResponse.amount)
            assertEquals(unit, ctx.meterReadingResponse.unit)
            assertEquals(meterId, ctx.meterReadingResponse.meterId)
            assertEquals(apartmentId, ctx.meterReadingResponse.apartmentId)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = MeterReadingContext(
            command = MeterCommand.READ,
            state = MeterReadingState.NONE,
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.BAD_ID,
            meterReadingRequest = MeterReading(),
        )
        processor.exec(ctx)
        assertEquals(MeterReading(), ctx.meterReadingResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
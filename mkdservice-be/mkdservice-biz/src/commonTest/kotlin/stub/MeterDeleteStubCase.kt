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

class MeterDeleteStubCase {

    private val processor = MkdMeterProcessor()
    val id = MeterReadingId(id = 1212)

    @Test
    fun delete() = runTest {

        val ctx = MeterReadingContext(
            command = MeterCommand.DELETE,
            state = MeterReadingState.NONE,
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.SUCCESS,
            meterReadingRequest = MeterReading(
                id = id,
            )
        )
        processor.exec(ctx)
        val stub = MeterReadingStub.get()
        assertEquals(id, ctx.meterReadingResponse.id)
        assertEquals(stub.amount, ctx.meterReadingResponse.amount)
        assertEquals(stub.unit, ctx.meterReadingResponse.unit)
        assertEquals(stub.apartmentId, ctx.meterReadingResponse.apartmentId)
        assertEquals(stub.dateTime, ctx.meterReadingResponse.dateTime)
        assertEquals(stub.meterId, ctx.meterReadingResponse.meterId)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = MeterReadingContext(
            command = MeterCommand.DELETE,
            state = MeterReadingState.NONE,
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.DB_ERROR,
            meterReadingRequest = MeterReading(
                id = id,
            )
        )
        processor.exec(ctx)
        assertEquals(MeterReading(), ctx.meterReadingResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }
}
package com.fedorovsky.mkdservice.biz.stub

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MeterCreateStubTest {

    private val processor = MkdMeterProcessor()
    val id = MeterReadingId(id = "1")
    val amount = Amount("150.08")
    val meterId = MeterId("3")
    val dateTime = "2025-07-02T00:00:00.000Z"
    val unit = MeterReadingUnit.M3
    val apartmentId = ApartmentId("5")

    @Test
    fun create() = runTest {

        val ctx = MeterReadingContext(
            command = MeterCommand.CREATE,
            state = MeterReadingState.NONE,
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.SUCCESS,
            meterReadingRequest = MeterReading(
                amount = amount,
                meterId = meterId,
                unit = unit,
                apartmentId = apartmentId,
            )
        )
        processor.exec(ctx)
        ctx.meterReadingResponse.id = id
        assertEquals(id, ctx.meterReadingResponse.id)
        assertEquals(amount, ctx.meterReadingResponse.amount)
        assertEquals(unit, ctx.meterReadingResponse.unit)
        assertEquals(apartmentId, ctx.meterReadingResponse.apartmentId)
        assertEquals(dateTime, ctx.meterReadingResponse.dateTime)
        assertEquals(meterId, ctx.meterReadingResponse.meterId)
    }

    @Test
    fun badAmount() = runTest {
        val ctx = MeterReadingContext(
            command = MeterCommand.CREATE,
            state = MeterReadingState.NONE,
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.BAD_AMOUNT,
            meterReadingRequest = MeterReading(
                amount = Amount("aabbcc"),
                meterId = meterId,
                unit = unit,
                apartmentId = apartmentId,
            )
        )
        processor.exec(ctx)
        assertEquals(MeterReading(), ctx.meterReadingResponse)
        assertEquals("amount", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals

fun validationAmountEmpty(command: MeterCommand, processor: MkdMeterProcessor) = runTest {
    val ctx = MeterReadingContext(
        command = command,
        state = MeterReadingState.NONE,
        workMode = MeterWorkMode.TEST,
        meterReadingRequest = MeterReading(
            amount = Amount(""),
            meterId = MeterId(2),
            unit = MeterReadingUnit.M3,
            apartmentId = ApartmentId(3),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MeterReadingState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("amount", error?.field)
    assertContains(error?.message ?: "", "amount")
}
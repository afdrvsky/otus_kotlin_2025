package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import com.fedorovsky.mkdservice.stubs.MeterReadingStub
import kotlinx.coroutines.test.runTest
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationIdCorrect(command: MeterCommand, processor: MkdMeterProcessor) = runTest {
    val ctx = MeterReadingContext(
        command = command,
        state = MeterReadingState.NONE,
        workMode = MeterWorkMode.TEST,
        meterReadingRequest = MeterReadingStub.get(),
    )
    processor.exec(ctx)
    print(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(MeterReadingState.FAILING, ctx.state)
}

fun validationIdEmpty(command: MeterCommand, processor: MkdMeterProcessor) = runTest {
    val ctx = MeterReadingContext(
        command = command,
        state = MeterReadingState.NONE,
        workMode = MeterWorkMode.TEST,
        meterReadingRequest = MeterReading(
            id = MeterReadingId.NONE,
        ),
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(MeterReadingState.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
package com.fedorovsky.mkdservice.api.v1.mappers

import com.fedorovsky.mkdservice.api.v1.models.*
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.*
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import com.fedorovsky.mkdservice.stubs.MeterReadingStub
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperUpdateTest {

    @Test
    fun fromTransport() {
        val req = MeterUpdateRequest(
            debug = MeterDebug(
                mode = MeterRequestDebugMode.STUB,
                stub = MeterRequestDebugStubs.SUCCESS,
            ),
            meter = MeterReadingStub.get().toTransportUpdateMeter(),
        )
        val expected = MeterReadingStub.prepareResult{
            dateTime = ""
            meterId = MeterId.NONE
            apartmentId = ApartmentId.NONE
        }

        val context = MeterReadingContext()
        context.fromTransport(req)

        assertEquals(MeterReadingStubs.SUCCESS, context.stubCase)
        assertEquals(MeterWorkMode.STUB, context.workMode)
        assertEquals(expected, context.meterReadingRequest)
    }

    @Test
    fun toTransport() {
        val context = MeterReadingContext(
            command = MeterCommand.UPDATE,
            state = MeterReadingState.RUNNING,
            errors = mutableListOf(
                MeterError(
                    code = "err",
                    group = "request",
                    field = "amount",
                    message = "wrong amount"
                )
            ),
            meterReadingResponse = MeterReadingStub.get()
        )

        val req = context.toTransportMeter() as MeterUpdateResponse

        assertEquals(req.meter, MeterReadingStub.get().toTransportMeter())
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("amount", req.errors?.firstOrNull()?.field)
        assertEquals("wrong amount", req.errors?.firstOrNull()?.message)
    }
}
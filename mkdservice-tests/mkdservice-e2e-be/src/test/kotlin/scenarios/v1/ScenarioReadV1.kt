package com.fedorovsky.mkdservice.e2e.be.scenarios.v1

import com.fedorovsky.mkdservice.api.v1.models.MeterCreateResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterDebug
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingCreateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadObject
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterResponseObject
import com.fedorovsky.mkdservice.api.v1.models.ResponseResult
import com.fedorovsky.mkdservice.e2e.be.base.client.Client
import com.fedorovsky.mkdservice.e2e.be.scenarios.v1.base.sendAndReceive
import com.fedorovsky.mkdservice.e2e.be.scenarios.v1.base.someCreateMeter
import com.fedorovsky.mkdservice.e2e.be.scenarios.v1.base.someDeleteMeter
import io.kotest.engine.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

abstract class ScenarioReadV1(
    private val client: Client,
    private val debug: MeterDebug? = null
) {
    @Test
    fun read() = runBlocking {
        val obj = someCreateMeter
        val delObj = someDeleteMeter
        val resCreate = client.sendAndReceive(
            "meter/create", MeterReadingCreateRequest(
                requestType = "create",
                debug = debug,
                meter = obj,
            )
        ) as MeterCreateResponse

        assertEquals(ResponseResult.SUCCESS, resCreate.result)

        val cObj: MeterResponseObject = resCreate.meter ?: fail("No meter in Create response")
        assertEquals(obj.amount, cObj.amount)
        assertEquals(obj.unit, cObj.unit)
        assertEquals(obj.meterId, cObj.meterId)
        assertEquals(obj.apartmentId, cObj.apartmentId)

        val rObj = MeterReadingReadObject(
            id = cObj.id,
        )
        val resRead = client.sendAndReceive(
            "meter/read",
            MeterReadingReadRequest(
                requestType = "read",
                debug = debug,
                meter = rObj,
            )
        ) as MeterReadResponse

        assertEquals(ResponseResult.SUCCESS, resRead.result)

        val rrObj: MeterResponseObject = resRead.meter ?: fail("No meter in Read response")
        assertEquals(obj.amount, rrObj.amount)
        assertEquals(obj.unit, rrObj.unit)
        assertEquals(obj.meterId, rrObj.meterId)
        assertEquals(obj.apartmentId, rrObj.apartmentId)
        assertNotNull(rrObj.id)
        assertNotNull(rrObj.dateTime)

        val resDelete = client.sendAndReceive(
            "meter/delete", MeterDeleteRequest(
                requestType = "delete",
                debug = debug,
                meter = delObj
            )
        ) as MeterDeleteResponse

        assertEquals(ResponseResult.SUCCESS, resDelete.result)

        val dObj: MeterResponseObject = resDelete.meter ?: fail("No meter in Delete response")
        assertEquals(delObj.id, dObj.id)
    }
}
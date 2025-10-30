package com.fedorovsky.mkdservice.e2e.be.scenarios.v1

import com.fedorovsky.mkdservice.api.v1.models.MeterCreateResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterDebug
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingCreateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterResponseObject
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateResponse
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

abstract class ScenarioUpdateV1(
    private val client: Client,
    private val debug: MeterDebug? = null
) {
    @Test
    fun update() = runBlocking {
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

        val uObj = MeterUpdateObject(
            amount = cObj.amount,
            unit = cObj.unit,
            id = "1",
            lock = cObj.lock,
        )
        val resUpdate = client.sendAndReceive(
            "meter/update",
            MeterUpdateRequest(
                requestType = "update",
                debug = debug,
                meter = uObj,
            )
        ) as MeterUpdateResponse

        assertEquals(ResponseResult.SUCCESS, resUpdate.result)

        val ruObj: MeterResponseObject = resUpdate.meter ?: fail("No meter in Update response")
        assertEquals(uObj.amount, ruObj.amount)
        assertEquals(uObj.unit, ruObj.unit)
        assertEquals(uObj.id, ruObj.id)
        assertNotNull( ruObj.apartmentId)
        assertNotNull(ruObj.id)
        assertNotNull(ruObj.dateTime)

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
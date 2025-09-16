package com.fedorovsky.mkdservice.app.ktor.stub

import com.fedorovsky.mkdservice.api.v1.apiV1Mapper
import com.fedorovsky.mkdservice.api.v1.models.IRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterCreateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterCreateResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterDebug
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteObject
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingCreateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadObject
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingReadRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterRequestDebugMode
import com.fedorovsky.mkdservice.api.v1.models.MeterRequestDebugStubs
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterUpdateResponse
import com.fedorovsky.mkdservice.app.ktor.MkdAppSettings
import com.fedorovsky.mkdservice.app.ktor.module
import com.fedorovsky.mkdservice.common.MkdCorSettings
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class V1MeterStubApiTest {

    @Test
    fun create() = v1TestApplication(
        func = "create",
        request = MeterReadingCreateRequest(
            meter = MeterCreateObject(
                amount = "100.01",
                unit = "m3",
                meterId = 3,
                apartmentId = 1,
            ),
            debug = MeterDebug(
                mode = MeterRequestDebugMode.STUB,
                stub = MeterRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<MeterCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(1, responseObj.meter?.apartmentId)
    }

    @Test
    fun read() = v1TestApplication(
        func = "read",
        request = MeterReadingReadRequest(
            meter = MeterReadingReadObject(1),
            debug = MeterDebug(
                mode = MeterRequestDebugMode.STUB,
                stub = MeterRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<MeterReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(3, responseObj.meter?.meterId)
    }

    @Test
    fun update() = v1TestApplication(
        func = "update",
        request = MeterUpdateRequest(
            meter = MeterUpdateObject(
                amount = "100.01",
                unit = "m3",
                meter = 1
            ),
            debug = MeterDebug(
                mode = MeterRequestDebugMode.STUB,
                stub = MeterRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<MeterUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(1, responseObj.meter?.id)
    }

    @Test
    fun delete() = v1TestApplication(
        func = "delete",
        request = MeterDeleteRequest(
            meter = MeterDeleteObject(
                id = 1,
            ),
            debug = MeterDebug(
                mode = MeterRequestDebugMode.STUB,
                stub = MeterRequestDebugStubs.SUCCESS
            )
        ),
    ) { response ->
        val responseObj = response.body<MeterDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(1, responseObj.meter?.id)
    }

    private inline fun <reified T: IRequest> v1TestApplication(
        func: String,
        request: T,
        crossinline function: suspend (HttpResponse) -> Unit,
    ): Unit = testApplication {
        application { module(MkdAppSettings(corSettings = MkdCorSettings())) }
        val client = createClient {
            install(ContentNegotiation) {
                json(apiV1Mapper)
            }
        }
        val response = client.post("/v1/meter/$func") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        function(response)
    }
}

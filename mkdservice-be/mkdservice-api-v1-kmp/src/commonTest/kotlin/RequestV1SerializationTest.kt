package com.fedorovsky.mkdservice.api.v1

import com.fedorovsky.mkdservice.api.v1.models.*
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request: IRequest = MeterReadingCreateRequest(
        debug = MeterDebug(
            mode = MeterRequestDebugMode.STUB,
            stub = MeterRequestDebugStubs.BAD_AMOUNT
        ),
        meter = MeterCreateObject(
            amount = "100.01",
            unit = "m3",
            meterId = "1",
            apartmentId = "10",
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"amount\":\\s*\"100.01\""))
        assertContains(json, Regex("\"unit\":\\s*\"m3\""))
        assertContains(json, Regex("\"meterId\":\\s*\"1\""))
        assertContains(json, Regex("\"apartmentId\":\\s*\"10\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badAmount\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(request)
        val obj = apiV1Mapper.decodeFromString<IRequest>(json) as MeterReadingCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"meter": null}
        """.trimIndent()
        val obj = apiV1Mapper.decodeFromString<MeterReadingCreateRequest>(jsonString)

        assertEquals(null, obj.meter)
    }

}
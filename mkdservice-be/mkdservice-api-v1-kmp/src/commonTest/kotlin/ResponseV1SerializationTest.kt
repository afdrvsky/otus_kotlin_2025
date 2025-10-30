package com.fedorovsky.mkdservice.api.v1

import com.fedorovsky.mkdservice.api.v1.models.IResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterCreateResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterResponseObject
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response: IResponse = MeterCreateResponse(
        meter = MeterResponseObject(
                amount = "100.01",
                unit = "m3",
                id = "1",
                dateTime = "20.07.2025 10:00",
                meterId = "100",
                apartmentId = "10"
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.encodeToString(response)

        println(json)

        assertContains(json, Regex("\"amount\":\\s*\"100.01\""))
        assertContains(json, Regex("\"unit\":\\s*\"m3"))
        assertContains(json, Regex("\"id\":\\s*\"1\""))
        assertContains(json, Regex("\"dateTime\":\\s*\"20.07.2025 10:00\""))
        assertContains(json, Regex("\"meterId\":\\s*\"100\""))
        assertContains(json, Regex("\"apartmentId\":\\s*\"10\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.encodeToString(response)
        val obj = apiV1Mapper.decodeFromString<IResponse>(json) as MeterCreateResponse

        assertEquals(response, obj)
    }
}
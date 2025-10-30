package com.fedorovsky.mkdservice.backend.repo.tests

import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.repo.common.IRepoMeterInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoMeterCreateTest {
    abstract val repo: IRepoMeterInitializable
    protected open val uuidNew = MeterReadingId("10000000-0000-0000-0000-000000000001")

    private val createObj = MeterReading(
        dateTime = "2025-07-19T00:00:00.000Z",
        amount = Amount("105.23"),
        unit = MeterReadingUnit.M3,
        meterId = MeterId("9"),
        apartmentId = ApartmentId("34"),
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createMeter(DbMeterRequest(createObj))
        val expected = createObj
        assertIs<DbMeterResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.dateTime, result.data.dateTime)
        assertEquals(expected.amount, result.data.amount)
        assertEquals(expected.unit, result.data.unit)
        assertEquals(expected.meterId, result.data.meterId)
        assertEquals(expected.apartmentId, result.data.apartmentId)
    }

    companion object : BaseInitMeters("create") {
        override val initObjects: List<MeterReading> = emptyList()
    }
}
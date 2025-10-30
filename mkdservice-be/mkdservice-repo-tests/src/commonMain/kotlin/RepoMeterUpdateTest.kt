package com.fedorovsky.mkdservice.backend.repo.tests

import com.fedorovsky.mkdservice.backend.repo.tests.RepoMeterCreateTest.Companion.initObjects
import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErr
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErrWithData
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoMeterUpdateTest {
    abstract val repo: IRepoMeter
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = MeterReadingId("meter-repo-update-not-found")
    protected val lockBad = MeterReadingLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = MeterReadingLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        MeterReading(
            id = updateSucc.id,
            dateTime = "2025-07-02T00:00:00.000Z",
            amount = Amount("170.08"),
            unit = MeterReadingUnit.M3,
            meterId = MeterId("3"),
            apartmentId = ApartmentId("1"),
            lock = initObjects.first().lock,
        )
    }
    private val reqUpdateNotFound = MeterReading(
        id = updateIdNotFound,
        dateTime = "2025-07-03T00:00:00.000Z",
        amount = Amount("171.08"),
        unit = MeterReadingUnit.M3,
        meterId = MeterId("3"),
        apartmentId = ApartmentId("1"),
        lock = initObjects.first().lock,
    )
    private val reqUpdateConc by lazy {
        MeterReading(
            id = updateConc.id,
            dateTime = "2025-07-04T00:00:00.000Z",
            amount = Amount("172.08"),
            unit = MeterReadingUnit.M3,
            meterId = MeterId("3"),
            apartmentId = ApartmentId("1"),
            lock = lockBad,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateMeter(DbMeterRequest(reqUpdateSucc))
        println("ERRORS: ${(result as? DbMeterResponseErr)?.errors}")
        println("ERRORSWD: ${(result as? DbMeterResponseErrWithData)?.errors}")
        assertIs<DbMeterResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.dateTime, result.data.dateTime)
        assertEquals(reqUpdateSucc.amount, result.data.amount)
        assertEquals(reqUpdateSucc.unit, result.data.unit)
        assertEquals(reqUpdateSucc.meterId, result.data.meterId)
        assertEquals(reqUpdateSucc.apartmentId, result.data.apartmentId)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateMeter(DbMeterRequest(reqUpdateNotFound))
        assertIs<DbMeterResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateMeter(DbMeterRequest(reqUpdateConc))
        assertIs<DbMeterResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitMeters("update") {
        override val initObjects: List<MeterReading> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
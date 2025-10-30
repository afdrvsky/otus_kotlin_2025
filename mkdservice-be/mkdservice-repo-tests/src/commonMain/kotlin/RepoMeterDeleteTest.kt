package com.fedorovsky.mkdservice.backend.repo.tests

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterIdRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErrWithData
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersResponseErr
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

abstract class RepoMeterDeleteTest {
    abstract val repo: IRepoMeter
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = MeterReadingId("meter-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        println("BEFORE $deleteSucc")
        val lockOld = deleteSucc.lock
        val result = repo.deleteMeter(DbMeterIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbMeterResponseOk>(result)
        println("AFTER ${result.data}")
        assertEquals(deleteSucc.id, result.data.id)
        assertEquals(deleteSucc.dateTime, result.data.dateTime)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readMeter(DbMeterFilterRequest(notFoundId))

        assertIs<DbMetersResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        println("ERROR ${result.errors}")
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteMeter(DbMeterIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbMeterResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }


    companion object : BaseInitMeters("delete") {
        override val initObjects: List<MeterReading> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
package com.fedorovsky.mkdservice.backend.repo.tests

import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMetersReponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersResponseErr
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

abstract class RepoMeterReadTest {
    abstract val repo: IRepoMeter
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readMeter(DbMeterFilterRequest(readSucc.id))

        println(result)

        assertIs<DbMetersReponseOk>(result)
        assertEquals(readSucc, result.data.first())
    }

    @Test
    fun readNotFound() = runRepoTest {
        println("REQUESTING")
        val result = repo.readMeter(DbMeterFilterRequest(notFoundId))
        println("RESULT: $result")

        assertIs<DbMetersResponseErr>(result)
        println("ERRORS: ${result.errors}")
        val error: MeterError? = result.errors.find { it.code == "repo-not-found" }
        assertEquals("rq", error?.field)
    }

    companion object : BaseInitMeters("read") {
        override val initObjects: List<MeterReading> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = MeterReadingId("meter-repo-read-notFound")

    }
}
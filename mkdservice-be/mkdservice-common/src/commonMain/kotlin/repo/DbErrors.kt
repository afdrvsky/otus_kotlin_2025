package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.helpers.errorSystem
import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.repo.exceptions.RepoConcyrrencyException
import com.fedorovsky.mkdservice.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: MeterReadingId) = DbMeterResponseErr(
    MeterError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: $id not found",
    )
)

fun errorNotFound(rq: DbMeterFilterRequest) = DbMetersResponseErr(
    listOf(
        MeterError(
            code = "$ERROR_GROUP_REPO-not-found",
            group = ERROR_GROUP_REPO,
            field = "rq",
            message = "Object with request: ${rq.meterId} or ${rq.meterReadingId} or ${rq.apartmentId} not found",
        )
    )
)

val errorEmptyId = DbMeterResponseErr(
    MeterError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank",
    )
)

fun errorRepoConcurrency(
    oldMeter: MeterReading,
    expectedLock: MeterReadingLock,
    exception: Exception = RepoConcyrrencyException(
        id = oldMeter.id,
        expectedLock = expectedLock,
        actualLock = oldMeter.lock,
    ),
) = DbMeterResponseErrWithData(
    meterReading = oldMeter,
    err = MeterError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldMeter.id} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: MeterReadingId) = DbMeterResponseErr(
    MeterError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Meter Reading $id is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbMeterResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
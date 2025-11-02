package com.fedorovsky.mkdservice.common.repo.exceptions

import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock

class RepoConcyrrencyException(id: MeterReadingId, expectedLock: MeterReadingLock, actualLock: MeterReadingLock?): RepoMeterException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
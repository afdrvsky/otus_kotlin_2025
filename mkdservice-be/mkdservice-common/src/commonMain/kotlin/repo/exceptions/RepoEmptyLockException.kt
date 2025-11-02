package com.fedorovsky.mkdservice.common.repo.exceptions

import com.fedorovsky.mkdservice.common.models.MeterReadingId

class RepoEmptyLockException(id: MeterReadingId): RepoMeterException(
    id,
    "Lock is empty in DB"
)
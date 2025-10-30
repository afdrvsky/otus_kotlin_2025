package com.fedorovsky.mkdservice.common.repo.exceptions

import com.fedorovsky.mkdservice.common.models.MeterReadingId

open class RepoMeterException(
    @Suppress("unused")
    val meterReadingId: MeterReadingId,
    msg: String,
): RepoException(msg)
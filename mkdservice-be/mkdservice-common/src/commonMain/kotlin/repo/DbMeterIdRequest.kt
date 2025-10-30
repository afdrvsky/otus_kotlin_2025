package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock

data class DbMeterIdRequest(
    val id: MeterReadingId,
    val lock: MeterReadingLock = MeterReadingLock.NONE,
) {
    constructor(meterReading: MeterReading): this(meterReading.id, meterReading.lock)
}

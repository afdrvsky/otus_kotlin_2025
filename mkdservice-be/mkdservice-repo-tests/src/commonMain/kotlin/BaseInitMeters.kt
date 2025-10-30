package com.fedorovsky.mkdservice.backend.repo.tests

import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit

abstract class BaseInitMeters(private val op: String): IInitObjects<MeterReading> {
    open val lockOld: MeterReadingLock = MeterReadingLock("1234")
    open val lockBad: MeterReadingLock = MeterReadingLock("1238")

    fun createInitTestModel(
        suf: String,
        lock: MeterReadingLock = lockOld,
    ) = MeterReading(
        id = MeterReadingId("meter-repo $op-$suf"),
        dateTime = "$suf stub 2025-07-09T00:00:00.000Z",
        amount = Amount("103.23"),
        unit = MeterReadingUnit.M3,
        meterId = MeterId("2"),
        apartmentId = ApartmentId("45"),
        lock = lock,
    )
}
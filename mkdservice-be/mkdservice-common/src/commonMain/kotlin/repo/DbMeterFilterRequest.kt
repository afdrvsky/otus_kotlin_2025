package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId

class DbMeterFilterRequest(
    val meterReadingId: MeterReadingId = MeterReadingId.NONE,
    val meterId: MeterId = MeterId.NONE,
    val apartmentId: ApartmentId = ApartmentId.NONE,
) {
    constructor(meterReading: MeterReading) : this(meterReading.id, meterReading.meterId, meterReading.apartmentId)
}
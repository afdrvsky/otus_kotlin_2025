package com.fedorovsky.mkdservice.stubs

import com.fedorovsky.mkdservice.common.models.*

object MeterReadingSendStub {
    val METER_READING_SEND: MeterReading
        get() = MeterReading(
            id = MeterReadingId(1),
            dateTime = "2025-07-02T00:00:00.000Z",
            amount = Amount("170.08"),
            unit = MeterReadingUnit.M3,
            meterId = MeterId(3),
            apartmentId = ApartmentId(1),
        )
}
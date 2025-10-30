package com.fedorovsky.mkdservice.e2e.be.scenarios.v1.base

import com.fedorovsky.mkdservice.api.v1.models.MeterCreateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterDeleteObject

val someCreateMeter = MeterCreateObject(
    amount = "55.9",
    unit = "M3",
    meterId = "2",
    apartmentId = "3"
)

val someDeleteMeter = MeterDeleteObject(
    id = "1",
    lock = "2"
)

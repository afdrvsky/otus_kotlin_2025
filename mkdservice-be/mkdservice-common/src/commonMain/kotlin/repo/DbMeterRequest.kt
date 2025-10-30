package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.models.MeterReading

data class DbMeterRequest(
    val meterReading: MeterReading
)
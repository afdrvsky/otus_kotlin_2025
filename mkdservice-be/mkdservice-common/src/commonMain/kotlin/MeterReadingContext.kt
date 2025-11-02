package com.fedorovsky.mkdservice.common

import com.fedorovsky.mkdservice.common.models.*
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import kotlinx.datetime.Instant

data class MeterReadingContext(
    var command: MeterCommand = MeterCommand.NONE,
    var state: MeterReadingState = MeterReadingState.NONE,
    val errors: MutableList<MeterError> = mutableListOf(),

    var corSettings: MkdCorSettings = MkdCorSettings(),
    var workMode: MeterWorkMode = MeterWorkMode.PROD,
    var stubCase: MeterReadingStubs = MeterReadingStubs.NONE,

    var requestId: MeterRequestId = MeterRequestId.NONE,

    var meterReadingRequest: MeterReading = MeterReading(),
    var timeStart: Instant = Instant.NONE,

    var meterReadingResponse: MeterReading = MeterReading(),
    var metersReadingResponse: List<MeterReading> = listOf(),

    var meterReadingValidating: MeterReading = MeterReading(),
    var meterReadingValidated: MeterReading = MeterReading(),

    var meterReadingRepo: IRepoMeter = IRepoMeter.NONE,
    var meterReadingRepoRead: MeterReading = MeterReading(),
    var metersReadingRepoRead: List<MeterReading> = listOf(),
    var meterReadingRepoPrepare: MeterReading = MeterReading(),
    var meterReadingRepoDone: MeterReading = MeterReading(),
    var metersReadingRepoDone: List<MeterReading> = listOf(),
)

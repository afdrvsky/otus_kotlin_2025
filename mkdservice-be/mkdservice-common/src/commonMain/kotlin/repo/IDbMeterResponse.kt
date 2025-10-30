package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReading

sealed interface IDbMeterResponse: IDbResponse<MeterReading>

data class DbMeterResponseOk(
    val data: MeterReading
): IDbMeterResponse

data class DbMeterResponseErr(
    val errors: List<MeterError> = emptyList()
): IDbMeterResponse {
    constructor(err: MeterError): this(listOf(err))
}

data class DbMeterResponseErrWithData(
    val data: MeterReading,
    val errors: List<MeterError> = emptyList()
): IDbMeterResponse {
    constructor(meterReading: MeterReading, err: MeterError): this(meterReading, listOf(err))
}
package com.fedorovsky.mkdservice.common.repo

import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReading

sealed interface IDbMetersResponse : IDbResponse<List<MeterReading>>

data class DbMetersReponseOk(
    val data: List<MeterReading>
) : IDbMetersResponse

@Suppress("unused")
data class DbMetersResponseErr(
    val errors: List<MeterError> = emptyList()
) : IDbMetersResponse {
    constructor(err: MeterError) : this(listOf(err))
}

data class DbMetersResponseErrWithData(
    val data: List<MeterReading>,
    val errors: List<MeterError> = emptyList()
) : IDbMetersResponse {
    constructor(metersReading: List<MeterReading>, err: MeterError) : this(metersReading, listOf(err))
}
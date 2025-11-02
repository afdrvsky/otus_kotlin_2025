package com.fedorovsky.mkdservice.backend.repo.tests

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterIdRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersReponseOk
import com.fedorovsky.mkdservice.common.repo.IDbMeterResponse
import com.fedorovsky.mkdservice.common.repo.IDbMetersResponse
import com.fedorovsky.mkdservice.common.repo.IRepoMeter

class MeterRepositoryMock(
    private val invokeCreateMeter: (DbMeterRequest) -> IDbMeterResponse = { DEFAULT_METER_SUCCESS_EMPTY_MOCK },
    private val invokeReadMeter: (DbMeterFilterRequest) -> IDbMetersResponse = { DEFAULT_METERS_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateMeter: (DbMeterRequest) -> IDbMeterResponse = { DEFAULT_METER_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteMeter: (DbMeterIdRequest) -> IDbMeterResponse = { DEFAULT_METER_SUCCESS_EMPTY_MOCK },
) : IRepoMeter {
    override suspend fun createMeter(rq: DbMeterRequest): IDbMeterResponse {
        return invokeCreateMeter(rq)
    }

    override suspend fun readMeter(rq: DbMeterFilterRequest): IDbMetersResponse {
        return invokeReadMeter(rq)
    }

    override suspend fun updateMeter(rq: DbMeterRequest): IDbMeterResponse {
        return invokeUpdateMeter(rq)
    }

    override suspend fun deleteMeter(rq: DbMeterIdRequest): IDbMeterResponse {
        return invokeDeleteMeter(rq)
    }

    companion object {
        val DEFAULT_METER_SUCCESS_EMPTY_MOCK = DbMeterResponseOk(MeterReading())
        val DEFAULT_METERS_SUCCESS_EMPTY_MOCK = DbMetersReponseOk(emptyList())
    }
}
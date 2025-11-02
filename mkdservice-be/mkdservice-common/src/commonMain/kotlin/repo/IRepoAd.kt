package com.fedorovsky.mkdservice.common.repo

interface IRepoMeter {
    suspend fun createMeter(rq: DbMeterRequest): IDbMeterResponse
    suspend fun readMeter(rq: DbMeterFilterRequest): IDbMetersResponse
    suspend fun updateMeter(rq: DbMeterRequest): IDbMeterResponse
    suspend fun deleteMeter(rq: DbMeterIdRequest): IDbMeterResponse
    companion object {
        val NONE = object : IRepoMeter {
            override suspend fun createMeter(rq: DbMeterRequest): IDbMeterResponse {
                throw NotImplementedError("Must not be used")
            }
            override suspend fun readMeter(rq: DbMeterFilterRequest): IDbMetersResponse {
                throw NotImplementedError("Must not be used")
            }
            override suspend fun deleteMeter(rq: DbMeterIdRequest): IDbMeterResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateMeter(rq: DbMeterRequest): IDbMeterResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
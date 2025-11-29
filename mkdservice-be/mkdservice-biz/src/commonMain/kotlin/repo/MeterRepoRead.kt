package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMetersReponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersResponseErr
import com.fedorovsky.mkdservice.common.repo.DbMetersResponseErrWithData
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение показания из БД"
    on { state == MeterReadingState.RUNNING }
    handle {
        val request = DbMeterFilterRequest(meterReadingValidated)
        when(val result = meterReadingRepo.readMeter(request)) {
            is DbMetersReponseOk -> metersReadingRepoRead = result.data
            is DbMetersResponseErr -> fail(result.errors)
            is DbMetersResponseErrWithData -> {
                fail(result.errors)
                metersReadingRepoRead = result.data
            }
        }
    }
}

fun ICorChainDsl<MeterReadingContext>.repoReadPrevious(title: String) = worker {
    this.title = title
    description = "Чтение предыдущего показания из БД"
    on { state == MeterReadingState.RUNNING }
    handle {
        val request = DbMeterFilterRequest(meterReadingValidating)
        val result: DbMetersReponseOk = meterReadingRepo.readMeter(request) as DbMetersReponseOk
        metersReadingRepoRead = result.data
    }
}

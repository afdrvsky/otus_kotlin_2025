package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErr
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErrWithData
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление данных о показании ПУ в БД"
    on { state == MeterReadingState.RUNNING }
    handle {
        val request = DbMeterRequest(meterReadingRepoPrepare)
        when(val result = meterReadingRepo.createMeter(request)) {
            is DbMeterResponseOk -> meterReadingRepoDone = result.data
            is DbMeterResponseErr -> fail(result.errors)
            is DbMeterResponseErrWithData -> fail(result.errors)
        }
    }
}
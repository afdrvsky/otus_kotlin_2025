package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterIdRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErr
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErrWithData
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == MeterReadingState.RUNNING }
    handle {
        val request = DbMeterIdRequest(meterReadingRepoPrepare)
        when(val result = meterReadingRepo.deleteMeter(request)) {
            is DbMeterResponseOk -> meterReadingRepoDone = result.data
            is DbMeterResponseErr -> {
                fail(result.errors)
                meterReadingRepoDone = meterReadingRepoRead
            }
            is DbMeterResponseErrWithData -> {
                fail(result.errors)
                meterReadingRepoDone = result.data
            }
        }
    }
}

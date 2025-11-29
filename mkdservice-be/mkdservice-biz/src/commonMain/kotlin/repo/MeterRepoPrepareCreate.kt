package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == MeterReadingState.RUNNING }
    handle {
        meterReadingRepoPrepare = meterReadingValidated.deepCopy()
//        meterReadingRepoPrepare.ownerId = principal.id
    }
}
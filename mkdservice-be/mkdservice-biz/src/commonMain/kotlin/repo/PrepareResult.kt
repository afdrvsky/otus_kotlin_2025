package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != MeterWorkMode.STUB }
    handle {
        meterReadingResponse = meterReadingRepoDone
        metersReadingResponse = metersReadingRepoDone
        state = when (val st = state) {
            MeterReadingState.RUNNING -> MeterReadingState.FINISHING
            else -> st
        }
    }
}
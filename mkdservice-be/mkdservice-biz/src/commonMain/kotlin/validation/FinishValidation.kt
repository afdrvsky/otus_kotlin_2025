package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.finishMeterValidation(title: String) = worker {
    this.title = title
    on { state == MeterReadingState.RUNNING }
    handle {
        meterReadingValidated = meterReadingValidating
    }
}
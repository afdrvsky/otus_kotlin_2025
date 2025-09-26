package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.chain

fun ICorChainDsl<MeterReadingContext>.validation(block: ICorChainDsl<MeterReadingContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MeterReadingState.RUNNING }
}
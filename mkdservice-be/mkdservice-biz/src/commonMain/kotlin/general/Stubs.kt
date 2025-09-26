package com.fedorovsky.mkdservice.biz.general

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.chain

fun ICorChainDsl<MeterReadingContext>.stubs(
    title: String,
    block: ICorChainDsl<MeterReadingContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { workMode == MeterWorkMode.STUB && state == MeterReadingState.RUNNING }
}
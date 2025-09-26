package com.fedorovsky.mkdservice.biz.general

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.chain

fun ICorChainDsl<MeterReadingContext>.operation(
    title: String,
    command: MeterCommand,
    block: ICorChainDsl<MeterReadingContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == MeterReadingState.RUNNING }
}
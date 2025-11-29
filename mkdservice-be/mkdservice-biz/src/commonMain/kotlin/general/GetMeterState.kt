package com.fedorovsky.mkdservice.biz.general

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.getMeterState(title: String) = worker {
    this.title = title
    this.description = """
        Получаем состояние из сервиса состояний
    """.trimIndent()
    on { state == MeterReadingState.RUNNING }
    handle {
        corSettings.stateSettings.stateMachine
    }
}
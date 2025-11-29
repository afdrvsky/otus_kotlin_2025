package com.fedorovsky.mkdservice.biz.statemachine.helper

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.IMkdStateContext
import com.fedorovsky.mkdservice.states.common.models.MkdState

fun <T: IMkdStateContext> ICorChainDsl<T>.initStatus(title: String) = worker() {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == MkdState.NONE }
    handle { state = MkdState.RUNNING }
}

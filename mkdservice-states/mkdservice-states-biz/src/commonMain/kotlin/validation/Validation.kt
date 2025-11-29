package com.fedorovsky.mkdservice.biz.statemachine.validation

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.chain
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.models.MkdState

fun ICorChainDsl<MkdStateContext>.validation(block: ICorChainDsl<MkdStateContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == MkdState.RUNNING }
}

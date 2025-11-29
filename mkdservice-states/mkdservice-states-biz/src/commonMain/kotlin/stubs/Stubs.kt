package com.fedorovsky.mkdservice.biz.statemachine.stubs

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.chain
import com.fedorovsky.mkdservice.states.common.MeterWorkMode
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.models.MkdState

fun ICorChainDsl<MkdStateContext>.stubs(title: String, block: ICorChainDsl<MkdStateContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == MeterWorkMode.STUB && state == MkdState.RUNNING }
}

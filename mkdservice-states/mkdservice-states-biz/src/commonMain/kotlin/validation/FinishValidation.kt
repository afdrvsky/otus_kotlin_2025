package com.fedorovsky.mkdservice.biz.statemachine.validation

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.models.MkdState

fun ICorChainDsl<MkdStateContext>.finishValidation(title: String) = worker {
    this.title = title
    on { state == MkdState.RUNNING }
    handle {
        rqValidated = rqValidating
    }
}

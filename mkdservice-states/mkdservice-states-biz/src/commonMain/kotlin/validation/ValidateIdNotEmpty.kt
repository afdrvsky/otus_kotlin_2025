package com.fedorovsky.mkdservice.biz.statemachine.validation

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.helpers.errorValidation
import com.fedorovsky.mkdservice.states.common.helpers.fail

fun ICorChainDsl<MkdStateContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { rqValidating.stateId.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "id",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
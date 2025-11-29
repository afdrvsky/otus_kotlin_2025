package com.fedorovsky.mkdservice.biz.statemachine.validation

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.helpers.errorValidation
import com.fedorovsky.mkdservice.states.common.helpers.fail
import com.fedorovsky.mkdservice.states.common.models.MkdMeterStateId

fun ICorChainDsl<MkdStateContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { rqValidating.stateId != MkdMeterStateId.NONE && !rqValidating.stateId.asString().matches(regExp) }
    handle {
        val encodedId = rqValidating.stateId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}

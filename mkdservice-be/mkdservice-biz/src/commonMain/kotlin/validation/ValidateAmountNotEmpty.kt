package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.errorValidation
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.validateAmountNotEmpty(title: String) = worker {
    this.title = title
    on { meterReadingValidating.amount.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "amount",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
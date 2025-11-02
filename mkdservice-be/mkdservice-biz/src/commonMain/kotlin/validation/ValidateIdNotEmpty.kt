package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.errorValidation
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.validateIdNotEmpty(title: String) = worker {
    this.title = title
    on { meterReadingValidating.id.asString().isEmpty() }
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
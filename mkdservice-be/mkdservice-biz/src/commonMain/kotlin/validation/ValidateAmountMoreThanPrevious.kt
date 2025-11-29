package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.errorValidation
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.validateAmountMoreThanPrevious(title: String) = worker {
    this.title = title
    on {
        (metersReadingRepoRead.lastOrNull()?.amount?.asString()?.toDouble()
            ?: 0.0) > meterReadingValidating.amount.asString()
            .toDouble()
    }
    handle {
        fail(
            errorValidation(
                field = "amount",
                violationCode = "size",
                description = "amount must be greater than or equal to previous",
            )
        )
    }
}
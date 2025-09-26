package com.fedorovsky.mkdservice.biz.stubs

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.stubValidationBadAmount(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для суммы
    """.trimIndent()
    on { stubCase == MeterReadingStubs.BAD_AMOUNT && state == MeterReadingState.RUNNING }
    handle {
        fail(
            MeterError(
                group = "validation",
                code = "validation-amount",
                field = "amount",
                message = "Wrong amount field"
            )
        )
    }
}
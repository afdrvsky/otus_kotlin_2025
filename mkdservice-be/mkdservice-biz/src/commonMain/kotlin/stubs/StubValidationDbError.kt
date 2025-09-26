package com.fedorovsky.mkdservice.biz.stubs

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.stubDbError(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки базы данных
    """.trimIndent()
    on { stubCase == MeterReadingStubs.DB_ERROR && state == MeterReadingState.RUNNING }
    handle {
        fail(
            MeterError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
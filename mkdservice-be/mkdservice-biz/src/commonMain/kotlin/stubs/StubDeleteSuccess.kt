package com.fedorovsky.mkdservice.biz.stubs

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.stubs.MeterReadingStubs
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.logging.common.LogLevel
import com.fedorovsky.mkdservice.stubs.MeterReadingStub

fun ICorChainDsl<MeterReadingContext>.stubDeleteSuccess(title: String, corSettings: MkdCorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления показания
    """.trimIndent()
    on { stubCase == MeterReadingStubs.SUCCESS && state == MeterReadingState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = MeterReadingState.FINISHING
            val stub = MeterReadingStub.prepareResult {
                meterReadingRequest.id.takeIf { it.toString().isNotBlank() }?.also { this.id = it }
            }
            meterReadingResponse = stub
        }
    }
}
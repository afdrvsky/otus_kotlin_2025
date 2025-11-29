package com.fedorovsky.mkdservice.biz.statemachine.general

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdStateContext

fun ICorChainDsl<MkdStateContext>.prepareResponse(title: String) = worker {
    this.title = title
    this.description = """
        Подготовка ответа
    """.trimIndent()
}

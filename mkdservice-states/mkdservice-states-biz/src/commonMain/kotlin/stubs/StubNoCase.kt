package com.fedorovsky.mkdservice.biz.statemachine.stubs

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.helpers.fail
import com.fedorovsky.mkdservice.states.common.models.MkdError
import com.fedorovsky.mkdservice.states.common.models.MkdState

fun ICorChainDsl<MkdStateContext>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == MkdState.RUNNING }
    handle {
        fail(
            MkdError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
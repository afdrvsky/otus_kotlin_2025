package com.fedorovsky.mkdservice.biz.statemachine.general

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.IMkdStateContext

fun <T: IMkdStateContext> ICorChainDsl<T>.initRepo(title: String) = worker {
    this.title = title
    this.description = """
        Вычисление актуального репозитория в зависимости от типа запроса
    """.trimIndent()
}

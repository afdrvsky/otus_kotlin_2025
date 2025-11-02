package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.common.repo.errorRepoConcurrency
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == MeterReadingState.RUNNING && meterReadingValidated.lock != metersReadingRepoRead.first().lock }
    handle {
        fail(errorRepoConcurrency(metersReadingRepoRead.first(), meterReadingValidated.lock).errors)
    }
}
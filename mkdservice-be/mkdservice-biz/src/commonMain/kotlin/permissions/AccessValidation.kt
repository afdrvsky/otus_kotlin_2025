package com.fedorovsky.mkdservice.biz.permissions

import com.fedorovsky.mkdservice.auth.checkPermitted
import com.fedorovsky.mkdservice.auth.resolveRelationsTo
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.accessViolation
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.chain
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.accessValidation(title: String) = chain {
    this.title = title
    description = "Вычисление прав доступа по группе принципала и таблице прав доступа"
    on { state == MeterReadingState.RUNNING }
    worker("Вычисление отношения объявления к принципалу") {
        meterReadingRepoRead.principalRelations = meterReadingValidating.resolveRelationsTo(principal)
    }
    worker("Вычисление доступа к объявлению") {
        permitted = checkPermitted(command, meterReadingRepoRead.principalRelations, permissionsChain)
//        print("--------------------------")
//        println(meterReadingRepoRead.principalRelations)
//        print("--------------------------")
//        println(permissionsChain)
    }
    worker {
        this.title = "Валидация прав доступа"
        description = "Проверка наличия прав для выполнения операции"
        on { !permitted }
        handle {
            fail(
                accessViolation(
                    principal = principal,
                    operation = command,
                    meterReadingId = meterReadingRepoRead.id,
                )
            )
        }
    }
}

package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == MeterReadingState.RUNNING }
    handle {
        meterReadingRepoPrepare = meterReadingRepoRead.deepCopy().apply {
            this.id = meterReadingValidated.id
            dateTime = meterReadingValidated.dateTime
            amount = meterReadingValidated.amount
            apartmentId = meterReadingValidated.apartmentId
            meterId = meterReadingValidated.meterId
            unit = meterReadingValidated.unit
            lock = meterReadingValidated.lock
        }
    }
}

package com.fedorovsky.mkdservice.biz.repo

import com.fedorovsky.mkdservice.biz.exceptions.MkdserviceDbNotConfiguredException
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.errorSystem
import com.fedorovsky.mkdservice.common.helpers.fail
import com.fedorovsky.mkdservice.common.models.MeterWorkMode
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы
    """.trimIndent()
    handle {
        meterReadingRepo = when {
            workMode == MeterWorkMode.TEST -> corSettings.repoTest
            workMode == MeterWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != MeterWorkMode.STUB && meterReadingRepo == IRepoMeter.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = MkdserviceDbNotConfiguredException(workMode)
            )
        )
    }
}

package com.fedorovsky.mkdservice.biz.permissions

import com.fedorovsky.mkdservice.auth.resolveFrontPermissions
import com.fedorovsky.mkdservice.auth.resolveRelationsTo
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker

fun ICorChainDsl<MeterReadingContext>.frontPermissions(title: String) = worker {
    this.title = title
    description = "Вычисление разрешений пользователей для фронтенда"

    on { state == MeterReadingState.RUNNING }

    handle {
        meterReadingRepoDone.permissionsClient.addAll(
            resolveFrontPermissions(
                permissionsChain,
                // Повторно вычисляем отношения, поскольку они могли измениться при выполении операции
                meterReadingRepoDone.resolveRelationsTo(principal)
            )
        )

        for (meter in metersReadingRepoDone) {
            meter.permissionsClient.addAll(
                resolveFrontPermissions(
                    permissionsChain,
                    meter.resolveRelationsTo(principal)
                )
            )
        }
    }
}

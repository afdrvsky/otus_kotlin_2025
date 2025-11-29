package com.fedorovsky.mkdservice.biz.statemachine.stubs

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MeterReadingStubs
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.models.MkdMeterStateId
import com.fedorovsky.mkdservice.states.common.models.MkdState
import com.fedorovsky.mkdservice.states.common.models.MkdStateRq
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates
import com.fedorovsky.mkdservice.states.common.statemachine.SMTransition
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

fun ICorChainDsl<MkdStateContext>.stubSuccess(title: String) = worker {
    this.title = title
    this.description = """
        Обрабатываем сценарий стаба с успешным запросом, когда возвращается объект с состоянием
    """.trimIndent()
    on { stubCase == MeterReadingStubs.SUCCESS && state == MkdState.RUNNING }
    handle {
        stateResponse = MkdStateRq(
            stateId = stateRequest.stateId.takeIf { it != MkdMeterStateId.NONE } ?: MkdMeterStateId("123"),
            oldState = SMMeterStates.ACTUAL,
            created = Clock.System.now() - 3.days,
            views = 10,
            transition = SMTransition.NONE
        )
        state = MkdState.FINISHING
    }
}

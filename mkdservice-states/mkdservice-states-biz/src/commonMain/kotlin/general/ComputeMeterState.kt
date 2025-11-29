package com.fedorovsky.mkdservice.biz.statemachine.general

import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdAllStatesContext
import com.fedorovsky.mkdservice.states.common.NONE
import com.fedorovsky.mkdservice.states.common.models.MkdState
import com.fedorovsky.mkdservice.states.common.models.MkdStateRq
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterSignal
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.reflect.KClass

private val clazz: KClass<*> = ICorChainDsl<MkdAllStatesContext>::computeMeterState::class
fun ICorChainDsl<MkdAllStatesContext>.computeMeterState(title: String) = worker {
    this.title = title
    this.description = "Вычисление состояния показания"
    on { state == MkdState.RUNNING }
    handle {
        val machine = this.corSettings.stateMachine
        val log = corSettings.loggerProvider.logger(clazz)
        statesComputed = statesWStat.onEach { sc: MkdStateRq ->
            val timeNow = Clock.System.now()
            val timePublished = sc.created.takeIf { it != Instant.NONE } ?: timeNow
            val signal = SMMeterSignal(
                state = sc.oldState.takeIf { it != SMMeterStates.NONE } ?: SMMeterStates.NEW,
                duration = timeNow - timePublished,
            )
            val transition = machine.resolve(signal)
            if (transition.state != sc.oldState) {
                log.info("New meter state transition: ${transition.description}")
            }
            sc.transition = transition
        }
    }
}

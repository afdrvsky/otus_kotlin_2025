package com.fedorovsky.mkdservice.biz.statemachine.resolver

import com.fedorovsky.mkdservice.biz.statemachine.MkdMeterStateProcessor
import com.fedorovsky.mkdservice.states.common.MeterReadingStubs
import com.fedorovsky.mkdservice.states.common.MeterWorkMode
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.MkdStatesCorSettings
import com.fedorovsky.mkdservice.states.common.models.MkdMeterStateId
import com.fedorovsky.mkdservice.states.common.models.MkdState
import com.fedorovsky.mkdservice.states.common.models.MkdStateRq
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterSignal
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

class SMMeterStateBizTest {

    @Test
    fun bizGetTest() = runTest {
        val machine = SMMeterStateResolverDefault()
        val settings = MkdStatesCorSettings(stateMachine = machine)
        val processor = MkdMeterStateProcessor(corSettings = settings)
        val ctx = MkdStateContext(
            workMode = MeterWorkMode.STUB,
            stubCase = MeterReadingStubs.SUCCESS,
            stateRequest = MkdStateRq(
                stateId = MkdMeterStateId("some")
            )
        )
        processor.exec(ctx)
        assertEquals(SMMeterStates.ACTUAL, ctx.stateResponse.oldState)
        assertContentEquals(emptyList(), ctx.errors)
        assertEquals(MkdState.FINISHING, ctx.state)
    }

    @Test
    fun new2actual() {
        val machine = SMMeterStateResolverDefault()
        val signal = SMMeterSignal(
            state = SMMeterStates.NEW,
            duration = 4.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(SMMeterStates.ACTUAL, transition.state)
    }
}
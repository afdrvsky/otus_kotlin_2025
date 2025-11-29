package com.fedorovsky.mkdservice.biz.statemachine.resolver

import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterSignal
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days

class SMMeterStateTest {

    @Test
    fun new2actual() {
        val machine = SMMeterStateResolverDefault()
        val signal = SMMeterSignal(
            state = SMMeterStates.NEW,
            duration = 4.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(SMMeterStates.ACTUAL, transition.state)
        assertContains(transition.description, "актуальное", ignoreCase = true)
    }

    @Test
    fun new2old() {
        val machine = SMMeterStateResolverDefault()
        val signal = SMMeterSignal(
            state = SMMeterStates.NEW,
            duration = 35.days,
        )
        val transition = machine.resolve(signal)
        assertEquals(SMMeterStates.OLD, transition.state)
        assertContains(transition.description, "старое", ignoreCase = true)
    }
}
package com.fedorovsky.mkdservice.states.common.models

import com.fedorovsky.mkdservice.states.common.NONE
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates
import com.fedorovsky.mkdservice.states.common.statemachine.SMTransition
import kotlinx.datetime.Instant

data class MkdStateRq(
    var stateId: MkdMeterStateId = MkdMeterStateId.NONE,
    var oldState: SMMeterStates = SMMeterStates.NONE,
    var created: Instant = Instant.NONE,
    var views: Int = 0,
    var transition: SMTransition = SMTransition.ERROR,
) {
    fun deepCopy() = copy()
}
package com.fedorovsky.mkdservice.states.common

import com.fedorovsky.mkdservice.states.common.models.MkdError
import com.fedorovsky.mkdservice.states.common.models.MkdState
import com.fedorovsky.mkdservice.states.common.models.MkdStateRq
import kotlinx.datetime.Instant

data class MkdStateContext(
    override var state: MkdState = MkdState.NONE,
    var workMode: MeterWorkMode = MeterWorkMode.PROD,
    var stubCase: MeterReadingStubs = MeterReadingStubs.NONE,
    override val errors: MutableList<MkdError> = mutableListOf(),

    override var corSettings: MkdStatesCorSettings = MkdStatesCorSettings(),

    override var timeStart: Instant = Instant.NONE,

    var stateRequest: MkdStateRq = MkdStateRq(),
    var rqValidating: MkdStateRq = MkdStateRq(),
    var rqValidated: MkdStateRq = MkdStateRq(),

    var stateRead: MkdStateRq = MkdStateRq(),
    var stateWStats: MkdStateRq = MkdStateRq(),

    var stateResponse: MkdStateRq = MkdStateRq(),
): IMkdStateContext
package com.fedorovsky.mkdservice.states.common

import com.fedorovsky.mkdservice.states.common.models.MkdError
import com.fedorovsky.mkdservice.states.common.models.MkdState
import kotlinx.datetime.Instant

interface IMkdStateContext {
    var state: MkdState
    val errors: MutableList<MkdError>

    var corSettings: MkdStatesCorSettings

    var timeStart: Instant
}
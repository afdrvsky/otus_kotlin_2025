package com.fedorovsky.mkdservice.states.common

import com.fedorovsky.mkdservice.states.common.models.MkdError
import com.fedorovsky.mkdservice.states.common.models.MkdState
import com.fedorovsky.mkdservice.states.common.models.MkdStateRq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Instant

data class MkdAllStatesContext(
    override var state: MkdState = MkdState.NONE,
    override val errors: MutableList<MkdError> = mutableListOf(),

    override var corSettings: MkdStatesCorSettings = MkdStatesCorSettings(),

    override var timeStart: Instant = Instant.NONE,

    var statesRead: Flow<MkdStateRq> = flowOf(),
    var statesWStat: Flow<MkdStateRq> = flowOf(),
    var statesComputed: Flow<MkdStateRq> = flowOf(),
    var statesUpdating: Flow<MkdStateRq> = flowOf(),
) : IMkdStateContext
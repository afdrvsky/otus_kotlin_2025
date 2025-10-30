package com.fedorovsky.mkdservice.states.common.statemachine

import kotlin.time.Duration

data class SMMeterSignal(
    val state: SMMeterStates,
    val duration: Duration,
    val views: Int,
)
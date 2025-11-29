package com.fedorovsky.mkdservice.states.common

import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider
import com.fedorovsky.mkdservice.states.common.statemachine.ISMMeterStateResolver

data class MkdStatesCorSettings(
    val loggerProvider: MkdLoggerProvider = MkdLoggerProvider(),
    val stateMachine: ISMMeterStateResolver = ISMMeterStateResolver.NONE,
) {
    companion object {
        val NONE = MkdStatesCorSettings()
    }
}
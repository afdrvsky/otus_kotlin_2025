package com.fedorovsky.mkdservice.states.common.statemachine

data class SMTransition(
    val state: SMMeterStates,
    val description: String,
) {
    companion object {
        val ERROR = SMTransition(SMMeterStates.ERROR, "Unprovided transition occurred")
        val NONE = SMTransition(SMMeterStates.NONE, "Empty Transition")
    }
}
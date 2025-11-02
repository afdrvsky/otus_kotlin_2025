package com.fedorovsky.mkdservice.states.common.statemachine

interface ISMMeterStateResolver {
    fun resolve(signal: SMMeterSignal): SMTransition

    companion object {
        val NONE = object: ISMMeterStateResolver {
            override fun resolve(signal: SMMeterSignal): SMTransition = SMTransition.ERROR
        }
    }
}
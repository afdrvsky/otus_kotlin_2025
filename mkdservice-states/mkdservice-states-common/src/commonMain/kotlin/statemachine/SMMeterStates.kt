package com.fedorovsky.mkdservice.states.common.statemachine

@Suppress("unused")
enum class SMMeterStates {
    NONE, // не инициализировано состояние
    DRAFT,    // черновик
    NEW,      // только что передано
    ACTUAL,   // актуальное
    OLD,      // старое показание

    ERROR,    // ошибки вычисления;
}
package com.fedorovsky.mkdservice.common.models

import com.fedorovsky.mkdservice.logging.common.LogLevel

data class MeterError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)

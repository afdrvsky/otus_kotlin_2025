package com.fedorovsky.mkdservice.common.models

data class MeterError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null,
)

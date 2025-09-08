package com.fedorovsky.mkdservice.common.helpers

import com.fedorovsky.mkdservice.common.models.MeterError

fun Throwable.asMkdError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = MeterError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
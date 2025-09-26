package com.fedorovsky.mkdservice.common.helpers

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.models.MeterError
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.logging.common.LogLevel

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

inline fun MeterReadingContext.addError(vararg error: MeterError) = errors.addAll(error)

inline fun MeterReadingContext.fail(error: MeterError) {
    addError(error)
    state = MeterReadingState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = MeterError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

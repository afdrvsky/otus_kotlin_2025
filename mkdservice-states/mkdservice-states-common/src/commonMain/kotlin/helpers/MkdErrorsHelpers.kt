package com.fedorovsky.mkdservice.states.common.helpers

import com.fedorovsky.mkdservice.logging.common.LogLevel
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.models.MkdError
import com.fedorovsky.mkdservice.states.common.models.MkdState

inline fun MkdStateContext.addError(error: MkdError) = errors.add(error)
inline fun MkdStateContext.addErrors(error: Collection<MkdError>) = errors.addAll(error)

inline fun MkdStateContext.fail(error: MkdError) {
    addError(error)
    state = MkdState.FAILING
}

inline fun MkdStateContext.fail(errors: Collection<MkdError>) {
    addErrors(errors)
    state = MkdState.FAILING
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
) = MkdError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = MkdError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
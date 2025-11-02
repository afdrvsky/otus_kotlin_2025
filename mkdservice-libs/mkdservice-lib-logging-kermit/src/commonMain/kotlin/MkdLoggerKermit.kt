package com.fedorovsky.mkdservice.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import com.fedorovsky.mkdservice.logging.common.IMkdLogWrapper
import kotlin.reflect.KClass

@Suppress("unused")
fun mkdLoggerKermit(loggerId: String): IMkdLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return MkdLoggerWrapperKermit(
        logger = logger,
        loggerId = loggerId,
    )
}

@Suppress("unused")
fun mkdLoggerKermit(cls: KClass<*>): IMkdLogWrapper {
    val logger = Logger(
        config = StaticConfig(
            minSeverity = Severity.Info,
        ),
        tag = "DEV"
    )
    return MkdLoggerWrapperKermit(
        logger = logger,
        loggerId = cls.qualifiedName?: "",
    )
}
package com.fedorovsky.mkdservice.logging.logback

import ch.qos.logback.classic.Logger
import com.fedorovsky.mkdservice.logging.common.IMkdLogWrapper
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Generate internal MpLogContext logger
 *
 * @param logger Logback instance from [LoggerFactory.getLogger()]
 */
fun mkdLoggerLogback(logger: Logger): IMkdLogWrapper = MkdLogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun mkdLoggerLogback(clazz: KClass<*>): IMkdLogWrapper = mkdLoggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun mkdLoggerLogback(loggerId: String): IMkdLogWrapper = mkdLoggerLogback(LoggerFactory.getLogger(loggerId) as Logger)

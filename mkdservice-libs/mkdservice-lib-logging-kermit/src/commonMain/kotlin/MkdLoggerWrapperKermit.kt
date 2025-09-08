package com.fedorovsky.mkdservice.logging.kermit

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.fedorovsky.mkdservice.logging.common.IMkdLogWrapper
import com.fedorovsky.mkdservice.logging.common.LogLevel

class MkdLoggerWrapperKermit(
    val logger: Logger,
    override val loggerId: String
) : IMkdLogWrapper {

    override fun log(
        msg: String,
        level: LogLevel,
        marker: String,
        e: Throwable?,
        data: Any?,
        objs: Map<String,Any>?
    ) {
        logger.log(
            severity = level.toKermit(),
            tag = marker,
            throwable = e,
            message = formatMessage(msg, data, objs),
        )
    }

    private fun LogLevel.toKermit() = when(this) {
        LogLevel.ERROR -> Severity.Error
        LogLevel.WARN -> Severity.Warn
        LogLevel.INFO -> Severity.Info
        LogLevel.DEBUG -> Severity.Debug
        LogLevel.TRACE -> Severity.Verbose
    }

    // TODO Нужно для data придумать сериализацию или трансформацию в map
    private inline fun formatMessage(
        msg: String = "",
        data: Any? = null,
        objs: Map<String,Any>? = null
    ): String {
        var message = msg
        data?.let {
            message += "\n" + data.toString()
        }
        objs?.forEach {
            message += "\n" + it.toString()
        }
        return message
    }

}
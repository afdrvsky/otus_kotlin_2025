package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider
import com.fedorovsky.mkdservice.logging.kermit.mkdLoggerKermit
import io.ktor.server.application.Application

fun Application.getLoggerProviderConf(): MkdLoggerProvider =
    when (val mode = environment.config.propertyOrNull("ktor.logger")?.getString()) {
        "kmp" -> MkdLoggerProvider { mkdLoggerKermit(it) }
//        "socket", "sock" -> getSocketLoggerProvider()
//        "logback", null -> MkdLoggerProvider { mpLoggerLogback(it) }
        else -> throw Exception("Logger $mode is not allowed. Additted values are kmp, socket and logback (default)")
    }
package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider
import io.ktor.server.application.Application

//fun Application.getSocketLoggerProvider(): MkdLoggerProvider {
//    val loggerSettings = environment.config.config("ktor.socketLogger").let { conf ->
//        SocketLoggerSettings(
//            host = conf.propertyOrNull("host")?.getString() ?: "127.0.0.1",
//            port = conf.propertyOrNull("port")?.getString()?.toIntOrNull() ?: 9002,
//        )
//    }
//    return MpLoggerProvider { mpLoggerSocket(it, loggerSettings) }
//}
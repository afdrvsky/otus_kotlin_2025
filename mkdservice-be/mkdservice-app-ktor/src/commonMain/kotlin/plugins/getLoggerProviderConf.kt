package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider
import io.ktor.server.application.*

expect fun Application.getLoggerProviderConf(): MkdLoggerProvider
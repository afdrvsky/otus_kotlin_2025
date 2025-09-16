package com.fedorovsky.mkdservice.common

import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider

data class MkdCorSettings(
    val loggerProvider: MkdLoggerProvider = MkdLoggerProvider()
) {
    companion object {
        val NONE = MkdCorSettings()
    }
}

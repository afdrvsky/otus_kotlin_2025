package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.app.ktor.MkdAppSettings
import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MkdCorSettings
import io.ktor.server.application.Application

fun Application.initAppSettings(): MkdAppSettings {
    val loggerProvider = getLoggerProviderConf()
    val corSettings = MkdCorSettings(
        loggerProvider = loggerProvider,
        repoTest = getDatabaseConf(MeterDbType.TEST),
        repoProd = getDatabaseConf(MeterDbType.PROD),
        repoStub = getDatabaseConf(MeterDbType.TEST),
    )
    return MkdAppSettings(
        appUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        corSettings = corSettings,
        processor = MkdMeterProcessor(corSettings),
    )
}
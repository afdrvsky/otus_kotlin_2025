package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import com.fedorovsky.mkdservice.repo.inmemory.MeterRepoInMemory
import io.ktor.server.application.Application
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

expect fun Application.getDatabaseConf(type: MeterDbType): IRepoMeter

enum class MeterDbType(val confName: String) {
    PROD("prod"), TEST("test")
}

fun Application.initInMemory(): IRepoMeter {
    val ttlSetting = environment.config.propertyOrNull("db.prod")?.getString()?.let {
        Duration.parse(it)
    }
    return MeterRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}


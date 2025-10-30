package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.app.ktor.configs.CassandraConfig
import com.fedorovsky.mkdservice.app.ktor.configs.ConfigPaths
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import io.ktor.server.application.Application

actual fun Application.getDatabaseConf(type: MeterDbType): IRepoMeter {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    return when (val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        else -> throw IllegalArgumentException(
            "$dbSettingPath has value of '$dbSetting', but it must be set in application.yml to one of: " +
                    "'inmemory'"
        )
    }
}

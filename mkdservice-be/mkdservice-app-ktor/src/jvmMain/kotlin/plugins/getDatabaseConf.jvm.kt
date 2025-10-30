package com.fedorovsky.mkdservice.app.ktor.plugins

import com.fedorovsky.mkdservice.app.ktor.configs.CassandraConfig
import com.fedorovsky.mkdservice.app.ktor.configs.ConfigPaths
import com.fedorovsky.mkdservice.backend.repo.cassandra.RepoMeterCassandra
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import io.ktor.server.application.Application
import kotlin.IllegalArgumentException

actual fun Application.getDatabaseConf(type: MeterDbType): IRepoMeter {
    val dbSettingPath = "${ConfigPaths.repository}.${type.confName}"
    return when (val dbSetting = environment.config.propertyOrNull(dbSettingPath)?.getString()?.lowercase()) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "cassandra", "nosql", "cass" -> initCassandra()
        else -> throw IllegalArgumentException(
            "$dbSettingPath has value of '$dbSetting', but it must be set in application.yml to one of: " +
                    "'inmemory', 'cassandra'"
        )
    }
}

private fun Application.initCassandra(): IRepoMeter {
    val config = CassandraConfig(environment.config)
    return RepoMeterCassandra(
        keyspaceName = config.keyspace,
        host = config.host,
        port = config.port,
        user = config.user,
        pass = config.pass,
    )
}

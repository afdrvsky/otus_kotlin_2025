package com.fedorovsky.mkdservice.backend.repo.cassandra.model

import com.fedorovsky.mkdservice.common.models.MeterReadingUnit

enum class MeterCassandraReadingUnit {
    M3,
    K_WH,
    GCAL,
}

fun MeterCassandraReadingUnit?.fromTransport() = when (this) {
    null -> MeterReadingUnit.NONE
    MeterCassandraReadingUnit.M3 -> MeterReadingUnit.M3
    MeterCassandraReadingUnit.GCAL -> MeterReadingUnit.GCAL
    MeterCassandraReadingUnit.K_WH -> MeterReadingUnit.K_WH
}

fun MeterReadingUnit.toTransport() = when (this) {
    MeterReadingUnit.NONE -> null
    MeterReadingUnit.M3 -> MeterCassandraReadingUnit.M3
    MeterReadingUnit.GCAL -> MeterCassandraReadingUnit.GCAL
    MeterReadingUnit.K_WH -> MeterCassandraReadingUnit.K_WH
}
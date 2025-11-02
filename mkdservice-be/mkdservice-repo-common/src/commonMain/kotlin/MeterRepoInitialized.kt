package com.fedorovsky.mkdservice.repo.common

import com.fedorovsky.mkdservice.common.models.MeterReading

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class MeterRepoInitialized(
    val repo: IRepoMeterInitializable,
    initObjects: Collection<MeterReading> = emptyList(),
): IRepoMeterInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<MeterReading> = save(initObjects).toList()
}
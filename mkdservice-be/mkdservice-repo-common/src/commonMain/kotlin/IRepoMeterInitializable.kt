package com.fedorovsky.mkdservice.repo.common

import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.repo.IRepoMeter

interface IRepoMeterInitializable: IRepoMeter {
    fun save(meters: Collection<MeterReading>): Collection<MeterReading>
}
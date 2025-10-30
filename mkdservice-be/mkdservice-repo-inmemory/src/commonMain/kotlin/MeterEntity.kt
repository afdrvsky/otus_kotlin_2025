package com.fedorovsky.mkdservice.repo.inmemory

import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit

data class MeterEntity(
    val id: String? = null,
    val dateTime: String? = null,
    val amount: String? = null,
    val unit: String? = null,
    val meterId: String? = null,
    val apartmentId: String? = null,
    val lock: String? = null,
) {
    constructor(model: MeterReading): this(
        id = model.id.takeIf { it != MeterReadingId.NONE }?.asString(),
        dateTime = model.dateTime.takeIf { it.isNotBlank() },
        amount = model.amount.takeIf { it != Amount.NONE }?.asString(),
        unit = model.unit.takeIf { it != MeterReadingUnit.NONE }?.name,
        meterId = model.meterId.takeIf { it != MeterId.NONE }?.asString(),
        apartmentId = model.apartmentId.takeIf { it != ApartmentId.NONE }?.asString(),
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = MeterReading(
        id = id?.let { MeterReadingId(it) }?: MeterReadingId.NONE,
        dateTime = dateTime?: "",
        amount = amount?.let { Amount(it) }?: Amount.NONE,
        unit = unit?.let { MeterReadingUnit.valueOf(it) }?: MeterReadingUnit.NONE,
        meterId = meterId?.let { MeterId(it) }?: MeterId.NONE,
        apartmentId = apartmentId?.let { ApartmentId(it) }?: ApartmentId.NONE,
        lock = lock?.let { MeterReadingLock(it) }?: MeterReadingLock.NONE,
    )
}
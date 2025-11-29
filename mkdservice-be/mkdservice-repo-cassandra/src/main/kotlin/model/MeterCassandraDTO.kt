package com.fedorovsky.mkdservice.backend.repo.cassandra.model

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit

@Entity
data class MeterCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey
    var id: String? = null,

    @field:CqlName(COLUMN_AMOUNT)
    val amount: String? = null,

    @field:CqlName(COLUMN_UNIT)
    val unit: MeterCassandraReadingUnit? = null,

    @field:CqlName(COLUMN_DATE_TIME)
    val dateTime: String? = null,

    @field:CqlName(COLUMN_METER_ID)
    val meterId: String? = null,

    @field:CqlName(COLUMN_APARTMENT_ID)
    val apartmentId: String? = null,

    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {

    constructor(meterReadingModel: MeterReading) : this(
        id = meterReadingModel.id.takeIf { it != MeterReadingId.NONE }?.asString(),
        amount = meterReadingModel.amount.takeIf { it != Amount.NONE }?.asString(),
        unit = meterReadingModel.unit.toTransport(),
        dateTime = meterReadingModel.dateTime.takeIf { it.isNotBlank() },
        meterId = meterReadingModel.meterId.takeIf { it != MeterId.NONE }?.asString(),
        apartmentId = meterReadingModel.apartmentId.takeIf { it != ApartmentId.NONE }?.asString(),
        lock = meterReadingModel.lock.takeIf { it != MeterReadingLock.NONE }?.asString(),
    )

    fun toMeterReadingModel(): MeterReading =
        MeterReading(
            id = id?.let { MeterReadingId(it) } ?: MeterReadingId.NONE,
            amount = amount?.let { Amount(it) } ?: Amount.NONE,
            unit = unit.fromTransport(),
            dateTime = dateTime ?: "",
            meterId = meterId?.let { MeterId(it) } ?: MeterId.NONE,
            apartmentId = apartmentId?.let { ApartmentId(it) } ?: ApartmentId.NONE,
            lock = lock?.let { MeterReadingLock(it) } ?: MeterReadingLock.NONE,
        )

    companion object {
        const val TABLE_NAME = "mkdservice_meter_readings"

        const val COLUMN_ID = "id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DATE_TIME = "date_time"
        const val COLUMN_METER_ID = "meter_id"
        const val COLUMN_APARTMENT_ID = "apartment_id"
        const val COLUMN_UNIT = "unit"
        const val COLUMN_LOCK = "lock"
    }
}
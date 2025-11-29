package com.fedorovsky.mkdservice.backend.repo.cassandra.model

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity

@Entity
data class MeterApartmentDTO (
    @field:CqlName(COLUMN_APARTMENT_ID)
    var apartmentId: String? = null,

    @field:CqlName(COLUMN_METER_ID)
    var meterId: String? = null,
) {
    companion object {
        const val TABLE_NAME = "mkdservice_meter_apartments"

        const val COLUMN_METER_ID = "meter_id"
        const val COLUMN_APARTMENT_ID = "apartment_id"
    }
}
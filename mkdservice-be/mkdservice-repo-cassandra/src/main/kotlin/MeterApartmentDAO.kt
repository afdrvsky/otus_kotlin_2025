package com.fedorovsky.mkdservice.backend.repo.cassandra

import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Select
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterApartmentDTO
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterApartmentDTO.Companion.COLUMN_APARTMENT_ID
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterApartmentDTO.Companion.COLUMN_METER_ID
import java.util.concurrent.CompletionStage

@Dao
interface MeterApartmentDAO {

    @Select(customWhereClause = "${COLUMN_APARTMENT_ID} = :apartmentId and ${COLUMN_METER_ID} = :meterId")
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(apartmentId: String, meterId: String): CompletionStage<MeterApartmentDTO>?
}
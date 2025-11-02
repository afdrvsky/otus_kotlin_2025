package com.fedorovsky.mkdservice.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.annotations.Dao
import com.datastax.oss.driver.api.mapper.annotations.Delete
import com.datastax.oss.driver.api.mapper.annotations.Insert
import com.datastax.oss.driver.api.mapper.annotations.Query
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes
import com.datastax.oss.driver.api.mapper.annotations.Update
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterCassandraDTO
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterCassandraDTO.Companion.COLUMN_LOCK
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import java.util.concurrent.CompletionStage

@Dao
interface MeterCassandraDAO {
    @Insert
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun create(dto: MeterCassandraDTO): CompletionStage<MeterCassandraDTO>

    @QueryProvider(providerClass = MeterCassandraReadProvider::class, entityHelpers = [MeterCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "LOCAL_QUORUM")
    fun read(filter: DbMeterFilterRequest): CompletionStage<Collection<MeterCassandraDTO>>

    @Update(customIfClause = "${COLUMN_LOCK} = :prevLock")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun update(dto: MeterCassandraDTO, prevLock: String): CompletionStage<AsyncResultSet>

    @Delete(customWhereClause = "id = :id", customIfClause = "${COLUMN_LOCK} = :prevLock", entityClass = [MeterCassandraDTO::class])
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun delete(id: String, prevLock: String): CompletionStage<AsyncResultSet>

    @Query("TRUNCATE ${MeterCassandraDTO.TABLE_NAME}")
    @StatementAttributes(consistencyLevel = "QUORUM")
    fun deleteAll()
}
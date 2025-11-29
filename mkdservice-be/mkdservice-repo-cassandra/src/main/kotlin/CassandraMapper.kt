package com.fedorovsky.mkdservice.backend.repo.cassandra

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace
import com.datastax.oss.driver.api.mapper.annotations.DaoTable
import com.datastax.oss.driver.api.mapper.annotations.Mapper

@Mapper
interface CassandraMapper {
    @DaoFactory
    fun meterDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): MeterCassandraDAO

    @DaoFactory
    fun meterApartmentDao(@DaoKeyspace keyspace: String, @DaoTable tableName: String): MeterApartmentDAO

    companion object{
        fun builder(session: CqlSession) = CassandraMapperBuilder(session)
    }
}
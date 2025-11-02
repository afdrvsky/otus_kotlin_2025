package com.fedorovsky.mkdservice.backend.repo.cassandra

import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.mapper.MapperContext
import com.datastax.oss.driver.api.mapper.entity.EntityHelper
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterCassandraDTO
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.function.BiConsumer

class MeterCassandraReadProvider(
    private val context: MapperContext,
    private val entityHelper: EntityHelper<MeterCassandraDTO>
) {
    fun read(filter: DbMeterFilterRequest): CompletionStage<Collection<MeterCassandraDTO>> {
        var select = entityHelper.selectStart().allowFiltering()

        if (filter.meterReadingId != MeterReadingId.NONE) {
            select = select
                .whereColumn(MeterCassandraDTO.COLUMN_ID)
                .isEqualTo(QueryBuilder.literal(filter.meterReadingId.asString(), context.session.context.codecRegistry))
        }
        if (filter.apartmentId != ApartmentId.NONE) {
            select = select
                .whereColumn(MeterCassandraDTO.COLUMN_APARTMENT_ID)
                .isEqualTo(QueryBuilder.literal(filter.apartmentId.asString(), context.session.context.codecRegistry))
        }
        if (filter.meterId != MeterId.NONE) {
            select = select
                .whereColumn(MeterCassandraDTO.COLUMN_METER_ID)
                .isEqualTo(QueryBuilder.literal(filter.meterId.asString(), context.session.context.codecRegistry))
        }

        val asyncFetcher = AsyncFetcher()

        context.session
            .executeAsync(select.build())
            .whenComplete(asyncFetcher)

        return asyncFetcher.stage
    }

    inner class AsyncFetcher : BiConsumer<AsyncResultSet?, Throwable?> {
        private val buffer = mutableListOf<MeterCassandraDTO>()
        private val future = CompletableFuture<Collection<MeterCassandraDTO>>()
        val stage: CompletionStage<Collection<MeterCassandraDTO>> = future

        override fun accept(resultSet: AsyncResultSet?, t: Throwable?) {
            when {
                t != null -> future.completeExceptionally(t)
                resultSet == null -> future.completeExceptionally(IllegalStateException("ResultSet should not be null"))
                else -> {
                    buffer.addAll(resultSet.currentPage().map { entityHelper.get(it, false) })
                    if (resultSet.hasMorePages())
                        resultSet.fetchNextPage().whenComplete(this)
                    else
                        future.complete(buffer)
                }
            }
        }
    }
}
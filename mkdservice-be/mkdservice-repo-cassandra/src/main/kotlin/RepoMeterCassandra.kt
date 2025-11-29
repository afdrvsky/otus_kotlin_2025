package com.fedorovsky.mkdservice.backend.repo.cassandra

import com.benasher44.uuid.uuid4
import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.internal.core.type.codec.extras.enums.EnumNameCodec
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterApartmentDTO
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterCassandraDTO
import com.fedorovsky.mkdservice.backend.repo.cassandra.model.MeterCassandraReadingUnit
import com.fedorovsky.mkdservice.common.helpers.errorValidation
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterIdRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseErr
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersReponseOk
import com.fedorovsky.mkdservice.common.repo.IDbMeterResponse
import com.fedorovsky.mkdservice.common.repo.IDbMetersResponse
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import com.fedorovsky.mkdservice.common.repo.MeterRepoBase
import com.fedorovsky.mkdservice.common.repo.errorDb
import com.fedorovsky.mkdservice.common.repo.errorNotBelongTo
import com.fedorovsky.mkdservice.common.repo.errorNotFound
import com.fedorovsky.mkdservice.common.repo.errorRepoConcurrency
import com.fedorovsky.mkdservice.repo.common.IRepoMeterInitializable
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import java.net.InetAddress
import java.net.InetSocketAddress
import java.time.LocalDateTime
import java.time.ZoneId

class RepoMeterCassandra(
    private val keyspaceName: String,
    private val host: String = "",
    private val port: Int = 9042,
    private val user: String = "cassandra",
    private val pass: String = "cassandra",
    private val dc: String = "dc1",
    private val randomUuid: () -> String = { uuid4().toString() },
) : MeterRepoBase(), IRepoMeter, IRepoMeterInitializable {
    private val codecRegistry by lazy {
        DefaultCodecRegistry("default").apply {
            register(EnumNameCodec(MeterCassandraReadingUnit::class.java))
        }
    }

    private val session by lazy {
        CqlSession.builder()
            .addContactPoints(parseAddresses(host, port))
            .withLocalDatacenter(dc)
            .withAuthCredentials(user, pass)
            .withCodecRegistry(codecRegistry)
            .withKeyspace(keyspaceName)
            .build()
    }

    private val mapper by lazy { CassandraMapper.builder(session).build() }

    private val dao by lazy {
        mapper.meterDao(keyspaceName, MeterCassandraDTO.TABLE_NAME)
    }

    private val daoMeterApartment by lazy {
        mapper.meterApartmentDao(keyspaceName, MeterApartmentDTO.TABLE_NAME)
    }

    fun clear() = dao.deleteAll()

    override fun save(meters: Collection<MeterReading>): Collection<MeterReading> = runBlocking {
        meters.onEach { dao.create(MeterCassandraDTO(it)).await() }
    }

    override suspend fun createMeter(rq: DbMeterRequest): IDbMeterResponse = tryMeterMethod {
        val new = rq.meterReading.copy(
            id = MeterReadingId(randomUuid()),
            lock = MeterReadingLock(randomUuid()),
            dateTime = LocalDateTime.now(ZoneId.of("Europe/Moscow")).toString(),
        )
        when {
            isMeterIdInApartment(new.apartmentId, new.meterId) -> run {
                dao.create(MeterCassandraDTO(new)).await()
                DbMeterResponseOk(new)
            }
            else -> errorNotBelongTo(new.apartmentId, new.meterId)
        }
    }

    override suspend fun readMeter(rq: DbMeterFilterRequest): IDbMetersResponse = tryMetersMethod {
        val found = dao.read(rq).await().sortedByDescending(MeterCassandraDTO::dateTime)
        DbMetersReponseOk(found.map { it.toMeterReadingModel() })
    }

    override suspend fun updateMeter(rq: DbMeterRequest): IDbMeterResponse = tryMeterMethod {
        val idStr = rq.meterReading.id
        val prevLock = rq.meterReading.lock.asString()
        val new = rq.meterReading.copy(lock = MeterReadingLock(randomUuid()))
        val dto = MeterCassandraDTO(new)

        val res: AsyncResultSet = dao.update(dto, prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(MeterCassandraDTO.COLUMN_LOCK) }
            ?.getString(MeterCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DbMeterResponseOk(new)
            resultField == null -> errorNotFound(rq.meterReading.id)
            else -> errorRepoConcurrency(
                oldMeter = dao.read(DbMeterFilterRequest(idStr)).await().first()?.toMeterReadingModel()
                    ?: throw Exception(
                        "Consistency DB problem: Object with ID ${idStr.asString()} and requested lock $prevLock " +
                                "was denied for update but the same object was not found in db at further request"
                    ),
                expectedLock = rq.meterReading.lock
            )
        }
    }

    override suspend fun deleteMeter(rq: DbMeterIdRequest): IDbMeterResponse = tryMeterMethod {
        val idStr = rq.id
        val prevLock = rq.lock.asString()
        val oldMeter = dao.read(DbMeterFilterRequest(idStr)).await().first()?.toMeterReadingModel()
            ?: return@tryMeterMethod errorNotFound(rq.id)
        val res = dao.delete(idStr.asString(), prevLock).await()
        val isSuccess = res.wasApplied()
        val resultField = res.one()
            ?.takeIf { it.columnDefinitions.contains(MeterCassandraDTO.COLUMN_LOCK) }
            ?.getString(MeterCassandraDTO.COLUMN_LOCK)
            ?.takeIf { it.isNotBlank() }
        when {
            isSuccess -> DbMeterResponseOk(oldMeter)
            resultField == null -> errorNotFound(rq.id)
            else -> errorRepoConcurrency(
                dao.read(DbMeterFilterRequest(idStr)).await().first()?.toMeterReadingModel() ?: throw Exception(
                    "Consistency DB problem: Object with ID $idStr and requested lock $prevLock " +
                            "was successfully read but was denied for delete"
                ),
                rq.lock
            )
        }
    }

    private fun parseAddresses(hosts: String, port: Int): Collection<InetSocketAddress> = hosts
        .split(Regex("""\s*,\s*"""))
        .map { InetSocketAddress(InetAddress.getByName(it), port) }

    private fun isMeterIdInApartment(apartmentId: ApartmentId, meterId: MeterId) =
        runBlocking {
            val res = daoMeterApartment.read(apartmentId.asString(), meterId.asString())?.await()
            res != null
        }
}
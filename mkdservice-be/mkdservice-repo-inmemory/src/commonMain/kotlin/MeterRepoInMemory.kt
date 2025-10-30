package com.fedorovsky.mkdservice.repo.inmemory

import com.benasher44.uuid.uuid4
import com.fedorovsky.mkdservice.common.models.ApartmentId
import com.fedorovsky.mkdservice.common.models.MeterId
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterIdRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersReponseOk
import com.fedorovsky.mkdservice.common.repo.IDbMeterResponse
import com.fedorovsky.mkdservice.common.repo.IDbMetersResponse
import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import com.fedorovsky.mkdservice.common.repo.MeterRepoBase
import com.fedorovsky.mkdservice.common.repo.errorDb
import com.fedorovsky.mkdservice.common.repo.errorEmptyId
import com.fedorovsky.mkdservice.common.repo.errorEmptyLock
import com.fedorovsky.mkdservice.common.repo.errorNotFound
import com.fedorovsky.mkdservice.common.repo.errorRepoConcurrency
import com.fedorovsky.mkdservice.common.repo.exceptions.RepoEmptyLockException
import com.fedorovsky.mkdservice.repo.common.IRepoMeterInitializable
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class MeterRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : MeterRepoBase(), IRepoMeter, IRepoMeterInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, MeterEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(meters: Collection<MeterReading>) = meters.map { meter ->
        val entity = MeterEntity(meter)
        require(entity.id != null)
        cache.put(entity.id, entity)
        meter
    }

    override suspend fun createMeter(rq: DbMeterRequest): IDbMeterResponse = tryMeterMethod {
        val key = randomUuid()
        val meterReading = rq.meterReading.copy(id = MeterReadingId(key), lock = MeterReadingLock(randomUuid()))
        val entity = MeterEntity(meterReading)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbMeterResponseOk(meterReading)
    }

    override suspend fun readMeter(rq: DbMeterFilterRequest): IDbMetersResponse = tryMetersMethod {
        val result: List<MeterReading> = cache.asMap().asSequence()
            .filter { entry ->
                rq.meterReadingId.takeIf { it != MeterReadingId.NONE }?.let {
                    it.asString() == entry.value.id
                } ?: true
            }
            .filter { entry ->
                rq.meterId.takeIf { it != MeterId.NONE }?.let {
                    it.asString() == entry.value.meterId
                } ?: true
            }
            .filter { entry ->
                rq.apartmentId.takeIf { it != ApartmentId.NONE }?.let {
                    it.asString() == entry.value.apartmentId
                } ?: true
            }
            .map { it.value.toInternal() }
            .toList()
        when {
            result.isEmpty() -> errorNotFound(rq)
            else -> DbMetersReponseOk(result)
        }
    }

    override suspend fun updateMeter(rq: DbMeterRequest): IDbMeterResponse = tryMeterMethod {
        val rqMeter = rq.meterReading
        val id = rqMeter.id.takeIf { it != MeterReadingId.NONE } ?: return@tryMeterMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqMeter.lock.takeIf { it != MeterReadingLock.NONE } ?: return@tryMeterMethod errorEmptyLock(id)

        mutex.withLock {
            val oldMeter = cache.get(key)?.toInternal()
            when {
                oldMeter == null -> errorNotFound(id)
                oldMeter.lock == MeterReadingLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldMeter.lock != oldLock -> errorRepoConcurrency(oldMeter, oldLock)
                else -> {
                    val newMeter = rqMeter.copy(lock = MeterReadingLock(randomUuid()))
                    val entity = MeterEntity(newMeter)
                    cache.put(key, entity)
                    DbMeterResponseOk(newMeter)
                }
            }
        }
    }

    override suspend fun deleteMeter(rq: DbMeterIdRequest): IDbMeterResponse = tryMeterMethod {
        val id = rq.id.takeIf { it != MeterReadingId.NONE } ?: return@tryMeterMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != MeterReadingLock.NONE } ?: return@tryMeterMethod errorEmptyLock(id)

        mutex.withLock {
            val oldMeter = cache.get(key)?.toInternal()
            when {
                oldMeter == null -> errorNotFound(id)
                oldMeter.lock == MeterReadingLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldMeter.lock != oldLock -> errorRepoConcurrency(oldMeter, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbMeterResponseOk(oldMeter)
                }
            }
        }
    }

}
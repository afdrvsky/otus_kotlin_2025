import com.fedorovsky.mkdservice.backend.repo.tests.MeterRepositoryMock
import com.fedorovsky.mkdservice.common.models.MeterReading
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.repo.DbMeterFilterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterIdRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterRequest
import com.fedorovsky.mkdservice.common.repo.DbMeterResponseOk
import com.fedorovsky.mkdservice.common.repo.DbMetersReponseOk
import com.fedorovsky.mkdservice.stubs.MeterReadingStub
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MeterRepositoryMockTest {
    private val repo = MeterRepositoryMock(
        invokeCreateMeter = { DbMeterResponseOk(MeterReadingStub.prepareResult { id = MeterReadingId("create") }) },
        invokeReadMeter = { DbMetersReponseOk(listOf(MeterReadingStub.prepareResult { id = MeterReadingId("read") })) },
        invokeUpdateMeter = { DbMeterResponseOk(MeterReadingStub.prepareResult { id = MeterReadingId("update") }) },
        invokeDeleteMeter = { DbMeterResponseOk(MeterReadingStub.prepareResult { id = MeterReadingId("delete") }) }
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createMeter(DbMeterRequest(MeterReading()))
        assertIs<DbMeterResponseOk>(result)
        assertEquals(MeterReadingId("create"), result.data.id)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readMeter(DbMeterFilterRequest())
        assertIs<DbMetersReponseOk>(result)
        assertEquals(MeterReadingId("read"), result.data.first().id)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateMeter(DbMeterRequest(MeterReading()))
        assertIs<DbMeterResponseOk>(result)
        assertEquals(MeterReadingId("update"), result.data.id)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteMeter(DbMeterIdRequest(MeterReading()))
        assertIs<DbMeterResponseOk>(result)
        assertEquals(MeterReadingId("delete"), result.data.id)
    }
}
import com.fedorovsky.mkdservice.backend.repo.tests.RepoMeterCreateTest
import com.fedorovsky.mkdservice.backend.repo.tests.RepoMeterDeleteTest
import com.fedorovsky.mkdservice.backend.repo.tests.RepoMeterReadTest
import com.fedorovsky.mkdservice.backend.repo.tests.RepoMeterUpdateTest
import com.fedorovsky.mkdservice.repo.common.MeterRepoInitialized
import com.fedorovsky.mkdservice.repo.inmemory.MeterRepoInMemory

class MeterRepoInMemoryCreateTest: RepoMeterCreateTest() {
    override val repo = MeterRepoInitialized(
        MeterRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class MeterRepoInMemoryReadTest: RepoMeterReadTest() {
    override val repo = MeterRepoInitialized(
        MeterRepoInMemory(),
        initObjects = initObjects,
    )
}

class MeterRepoInMemoryUpdateTest: RepoMeterUpdateTest() {
    override val repo = MeterRepoInitialized(
        MeterRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}

class MeterRepoInMemoryDeleteTest: RepoMeterDeleteTest() {
    override val repo = MeterRepoInitialized(
        MeterRepoInMemory(),
        initObjects = initObjects,
    )
}
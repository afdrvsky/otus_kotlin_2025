package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.repo.common.MeterRepoInitialized
import com.fedorovsky.mkdservice.repo.inmemory.MeterRepoInMemory
import com.fedorovsky.mkdservice.stubs.MeterReadingStub

abstract class BaseBizValidationTest {
    protected abstract val command: MeterCommand
    private val repo = MeterRepoInitialized(
        repo = MeterRepoInMemory(),
        initObjects = listOf(
            MeterReadingStub.get(),
        ),
    )
    private val settings by lazy { MkdCorSettings(repoTest = repo) }
    protected val processor by lazy { MkdMeterProcessor(settings) }
}
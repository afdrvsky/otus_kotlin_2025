package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.common.models.MeterCommand

abstract class BaseBizValidationTest {
    protected abstract val command: MeterCommand
    private val settings by lazy { MkdCorSettings() }
    protected val processor by lazy { MkdMeterProcessor(settings) }
}
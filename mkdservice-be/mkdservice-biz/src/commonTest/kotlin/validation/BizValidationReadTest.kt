package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.models.MeterCommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = MeterCommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
}
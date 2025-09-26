package com.fedorovsky.mkdservice.biz.validation

import com.fedorovsky.mkdservice.common.models.MeterCommand
import kotlin.test.Test

class BizValidationCreateTest: BaseBizValidationTest() {
    override val command = MeterCommand.CREATE

    @Test fun emptyAmount() = validationAmountEmpty(command, processor)
}
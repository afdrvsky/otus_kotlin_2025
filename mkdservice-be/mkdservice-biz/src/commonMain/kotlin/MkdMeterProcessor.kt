package com.fedorovsky.mkdservice.biz

import com.fedorovsky.mkdservice.biz.general.initStatus
import com.fedorovsky.mkdservice.biz.general.operation
import com.fedorovsky.mkdservice.biz.general.stubs
import com.fedorovsky.mkdservice.biz.stubs.stubCreateSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubDbError
import com.fedorovsky.mkdservice.biz.stubs.stubDeleteSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubReadSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubUpdateSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubValidationBadAmount
import com.fedorovsky.mkdservice.biz.stubs.stubValidationBadId
import com.fedorovsky.mkdservice.biz.validation.finishMeterValidation
import com.fedorovsky.mkdservice.biz.validation.validateAmountNotEmpty
import com.fedorovsky.mkdservice.biz.validation.validateIdNotEmpty
import com.fedorovsky.mkdservice.biz.validation.validation
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.cor.rootChain
import com.fedorovsky.mkdservice.cor.worker

class MkdMeterProcessor(
    private val corSettings: MkdCorSettings = MkdCorSettings.NONE
) {

    suspend fun exec(ctx: MeterReadingContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MeterReadingContext> {
        initStatus("Инициализация статуса")

        operation("Передача показания ПУ", MeterCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadAmount("Имитация ошибки валидации суммы")
            }
            validation {
                worker("Копируем поля в meterValidating") { meterReadingValidating = meterReadingRequest.deepCopy() }
                validateAmountNotEmpty("Проверка, что поле сумма не пусто")
                finishMeterValidation("Завершение проверок")
            }
        }
        operation("Получить показания", MeterCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
            }
            validation {
                worker("Копируем поля в meterValidating") { meterValidating = meterReadingRequest.deepCopy() }
                worker("Очистка id") { meterValidating.id = MeterReadingId.NONE }
                validateIdNotEmpty("Проверка на непустой id")
                finishMeterValidation("Завершение проверок")
            }
        }
        operation("Обновить показания", MeterCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
            }
        }
        operation("Удалить показания", MeterCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubDbError("Имитация ошибки работы с БД")
            }
        }
    }.build()
}
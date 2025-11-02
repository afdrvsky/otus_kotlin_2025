package com.fedorovsky.mkdservice.biz

import com.fedorovsky.mkdservice.biz.general.initStatus
import com.fedorovsky.mkdservice.biz.general.operation
import com.fedorovsky.mkdservice.biz.general.stubs
import com.fedorovsky.mkdservice.biz.repo.checkLock
import com.fedorovsky.mkdservice.biz.repo.initRepo
import com.fedorovsky.mkdservice.biz.repo.prepareResult
import com.fedorovsky.mkdservice.biz.repo.repoCreate
import com.fedorovsky.mkdservice.biz.repo.repoDelete
import com.fedorovsky.mkdservice.biz.repo.repoPrepareCreate
import com.fedorovsky.mkdservice.biz.repo.repoPrepareDelete
import com.fedorovsky.mkdservice.biz.repo.repoPrepareUpdate
import com.fedorovsky.mkdservice.biz.repo.repoRead
import com.fedorovsky.mkdservice.biz.repo.repoUpdate
import com.fedorovsky.mkdservice.biz.stubs.stubCreateSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubDbError
import com.fedorovsky.mkdservice.biz.stubs.stubDeleteSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubReadSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubUpdateSuccess
import com.fedorovsky.mkdservice.biz.stubs.stubValidationBadAmount
import com.fedorovsky.mkdservice.biz.stubs.stubValidationBadId
import com.fedorovsky.mkdservice.biz.validation.finishMeterValidation
import com.fedorovsky.mkdservice.biz.validation.validateAmountNotEmpty
import com.fedorovsky.mkdservice.biz.validation.validation
import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.common.models.Amount
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterReadingId
import com.fedorovsky.mkdservice.common.models.MeterReadingLock
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import com.fedorovsky.mkdservice.cor.chain
import com.fedorovsky.mkdservice.cor.rootChain
import com.fedorovsky.mkdservice.cor.worker

class MkdMeterProcessor(
    private val corSettings: MkdCorSettings = MkdCorSettings.NONE
) {

    suspend fun exec(ctx: MeterReadingContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MeterReadingContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        operation("Передача показания ПУ", MeterCommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadAmount("Имитация ошибки валидации суммы")
            }
            validation {
                worker("Копируем поля в meterValidating") { meterReadingValidating = meterReadingRequest.deepCopy() }
                worker("Очистка id") { meterReadingValidating.id = MeterReadingId.NONE }
                worker("Очистка суммы") {
                    meterReadingValidating.amount = Amount(meterReadingValidating.amount.asString().trim())
                }
                validateAmountNotEmpty("Проверка, что поле сумма не пусто")
                finishMeterValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание объекта в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Получить показания", MeterCommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
            }
            validation {
                worker("Копируем поля в meterValidating") { meterReadingValidating = meterReadingRequest.deepCopy() }
//                worker("Очистка id") {
//                    meterReadingValidating.id = MeterReadingId(meterReadingValidating.id.asString().trim())
//                }
//                validateIdNotEmpty("Проверка на непустой id")
                finishMeterValidation("Завершение проверок")
            }
            chain {
                title = "Логика чтения"
                repoRead("Чтение объявления из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == MeterReadingState.RUNNING }
                    handle { metersReadingRepoDone = metersReadingRepoRead }
                }
            }
            prepareResult("Подготовка ответа")
        }
        operation("Обновить показания", MeterCommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
            }
            validation {
                worker("Копируем поля в meterValidating") { meterReadingValidating = meterReadingRequest.deepCopy() }
                worker("Очистка lock") {
                    meterReadingValidating.lock = MeterReadingLock(meterReadingValidating.lock.asString().trim())
                }
                finishMeterValidation("Завершение проверок")
            }
            chain {
                title = "Логика сохранения"
                repoRead("Чтение показания из БД")
//                accessValidation("Вычисление прав доступа")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление показания в БД")
            }
            prepareResult("Подготовка ответа")
        }
        operation("Удалить показания", MeterCommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubDbError("Имитация ошибки работы с БД")
            }
            validation {
                worker("Копируем поля в adValidating") {
                    meterReadingValidating = meterReadingRequest.deepCopy()
                }
                worker("Очистка id") {
                    meterReadingValidating.id = MeterReadingId(meterReadingValidating.id.asString().trim())
                }
                worker("Очистка lock") {
                    meterReadingValidating.lock = MeterReadingLock(meterReadingValidating.lock.asString().trim())
                }
                finishMeterValidation("Успешное завершение процедуры валидации")
            }
            chain {
                title = "Логика удаления"
                repoRead("Чтение показания из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление объявления из БД")
            }
            prepareResult("Подготовка ответа")
        }
    }.build()
}
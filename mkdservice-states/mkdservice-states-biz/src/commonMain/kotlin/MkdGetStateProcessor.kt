package com.fedorovsky.mkdservice.biz.statemachine

import com.fedorovsky.mkdservice.biz.statemachine.general.initRepo
import com.fedorovsky.mkdservice.biz.statemachine.general.prepareResponse
import com.fedorovsky.mkdservice.biz.statemachine.general.readStateFromDb
import com.fedorovsky.mkdservice.biz.statemachine.helper.initStatus
import com.fedorovsky.mkdservice.biz.statemachine.stubs.stubNoCase
import com.fedorovsky.mkdservice.biz.statemachine.stubs.stubSuccess
import com.fedorovsky.mkdservice.biz.statemachine.stubs.stubs
import com.fedorovsky.mkdservice.biz.statemachine.validation.finishValidation
import com.fedorovsky.mkdservice.biz.statemachine.validation.validateIdNotEmpty
import com.fedorovsky.mkdservice.biz.statemachine.validation.validateIdProperFormat
import com.fedorovsky.mkdservice.biz.statemachine.validation.validation
import com.fedorovsky.mkdservice.cor.rootChain
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdStateContext
import com.fedorovsky.mkdservice.states.common.MkdStatesCorSettings
import com.fedorovsky.mkdservice.states.common.models.MkdMeterStateId

class MkdMeterStateProcessor(
    private val corSettings: MkdStatesCorSettings = MkdStatesCorSettings.NONE
) {
    suspend fun exec(ctx: MkdStateContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MkdStateContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        stubs("Обработка стабов") {
            stubSuccess("Успешный сценарий")
            stubNoCase("Ошибка: запрошенный стаб недопустим")
        }
        validation {
            worker("Копируем поля в adValidating") { rqValidating = stateRequest.deepCopy() }
            worker("Очистка id") { rqValidating.stateId = MkdMeterStateId(rqValidating.stateId.asString().trim()) }
            validateIdNotEmpty("Проверка на непустой id")
            validateIdProperFormat("Проверка формата id")

            finishValidation("Успешное завершение процедуры валидации")
        }
        readStateFromDb("Чтение состояния из БД")
        prepareResponse("Подготовка ответа")
    }.build()
}
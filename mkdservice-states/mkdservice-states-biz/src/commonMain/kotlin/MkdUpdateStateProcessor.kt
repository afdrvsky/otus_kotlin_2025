package com.fedorovsky.mkdservice.biz.statemachine

import com.fedorovsky.mkdservice.biz.statemachine.general.computeMeterState
import com.fedorovsky.mkdservice.biz.statemachine.general.initRepo
import com.fedorovsky.mkdservice.biz.statemachine.helper.initStatus
import com.fedorovsky.mkdservice.cor.ICorChainDsl
import com.fedorovsky.mkdservice.cor.rootChain
import com.fedorovsky.mkdservice.cor.worker
import com.fedorovsky.mkdservice.states.common.MkdAllStatesContext
import com.fedorovsky.mkdservice.states.common.MkdStatesCorSettings

class MkdUpdateStateProcessor(
    private val corSettings: MkdStatesCorSettings = MkdStatesCorSettings.NONE,
) {
    suspend fun exec(ctx: MkdAllStatesContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })

    private val businessChain = rootChain<MkdAllStatesContext> {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

        readAllStatesWithLock("Чтение и блокировка всех объектов в БД на время вычисления")
        requestStatisticsServer("Запрос сервера статистики")
        computeMeterState("Вычисление обновленного состояния")
        updateStatesWithLock("Чтение и блокировка всех объектов в БД на время вычисления")
    }.build()
}

private fun ICorChainDsl<MkdAllStatesContext>.readAllStatesWithLock(title: String) {
    this.title = title
    this.description = """
        Запрашиваем все объекы из БД
        Проверяем блокировку и время последнего обновления
        - Если блокировка не установлена, ставим свою
        - Если блокировка установлена давно, переписываем на свою
        - Иначе пропускаем, она захвачена другим процессом
        Прочитанные объекты направляем во flow 
    """.trimIndent()
}

private fun ICorChainDsl<MkdAllStatesContext>.requestStatisticsServer(title: String) = worker {
    this.title = title
    this.description = """
        Выполняет батчевые запросы на сервер статистики (в мониторинг, OpenSearch)
    """.trimIndent()
}

private fun ICorChainDsl<MkdAllStatesContext>.updateStatesWithLock(title: String) = worker {
    this.title = title
    this.description = """
        Читает поток вычисленных обновлений и сохраняет их в БД с учетом блокировки.
        Там, где блокировки изменилась, такие объекты пропускаем, их перехватил другой процесс
    """.trimIndent()
}

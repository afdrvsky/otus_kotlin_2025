package com.fedorovsky.mkdservice.app.common

import com.fedorovsky.mkdservice.common.MeterReadingContext
import com.fedorovsky.mkdservice.common.helpers.asMkdError
import com.fedorovsky.mkdservice.common.models.MeterCommand
import com.fedorovsky.mkdservice.common.models.MeterReadingState
import kotlinx.datetime.Clock
import kotlin.reflect.KClass

suspend inline fun <T> IMkdAppSettings.controllerHelper(
    crossinline getRequest: suspend MeterReadingContext.() -> Unit,
    crossinline toResponse: suspend MeterReadingContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = MeterReadingContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
//            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
//            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
//            data = ctx.toLog(logId)
        )
        ctx.state = MeterReadingState.FAILING
        ctx.errors.add(e.asMkdError())
        processor.exec(ctx)
        if (ctx.command == MeterCommand.NONE) {
            ctx.command = MeterCommand.READ
        }
        ctx.toResponse()
    }
}
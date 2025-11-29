package com.fedorovsky.mkdservice.biz.statemachine.resolver

import com.fedorovsky.mkdservice.states.common.statemachine.ISMMeterStateResolver
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterSignal
import com.fedorovsky.mkdservice.states.common.statemachine.SMMeterStates
import com.fedorovsky.mkdservice.states.common.statemachine.SMTransition
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class SMMeterStateResolverDefault: ISMMeterStateResolver {
    override fun resolve(signal: SMMeterSignal): SMTransition {
        require(signal.duration >= 0.milliseconds) { "Publication duration cannot be negative" }
        val sig = Sig(
            st = signal.state,
            dur = SMDurs.entries.first { signal.duration >= it.min && signal.duration < it.max },
        )

        return TR_MX[sig] ?: SMTransition.ERROR
    }

    companion object {
        private enum class SMDurs(val min: Duration, val max: Duration) {
            D_NEW(0.seconds, 3.days),
            D_ACT(3.days, 14.days),
            D_OLD(30.days, Int.MAX_VALUE.seconds),
        }

        private enum class SMViews(val min: Int, val max: Int) { FEW(0, 30), MODER(30, 100), LARGE(100, Int.MAX_VALUE) }
        private data class Sig(
            val st: SMMeterStates,
            val dur: SMDurs,
        )

        private val TR_MX = mapOf(
            Sig(SMMeterStates.NEW, SMDurs.D_NEW) to SMTransition(SMMeterStates.NEW, "Новое без изменений"),
            Sig(SMMeterStates.NEW, SMDurs.D_ACT) to SMTransition(SMMeterStates.ACTUAL, "Вышло время, перевод из нового в актуальное"),
            Sig(SMMeterStates.NEW, SMDurs.D_OLD) to SMTransition(
                SMMeterStates.OLD,
                "Устарело, старое показание"
            ),
        )
    }
}
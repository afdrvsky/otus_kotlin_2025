package com.fedorovsky.mkdservice.common

import com.fedorovsky.mkdservice.common.repo.IRepoMeter
import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider
import com.fedorovsky.mkdservice.states.common.MkdStatesCorSettings

data class MkdCorSettings(
    val loggerProvider: MkdLoggerProvider = MkdLoggerProvider(),
    val repoTest: IRepoMeter = IRepoMeter.NONE,
    val repoStub: IRepoMeter = IRepoMeter.NONE,
    val repoProd: IRepoMeter = IRepoMeter.NONE,
    val stateSettings: MkdStatesCorSettings = MkdStatesCorSettings(),
) {
    companion object {
        val NONE = MkdCorSettings()
    }
}

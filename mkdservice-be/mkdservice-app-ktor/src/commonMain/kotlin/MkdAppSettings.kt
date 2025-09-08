package com.fedorovsky.mkdservice.app.ktor

import com.fedorovsky.mkdservice.app.common.IMkdAppSettings
import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MkdCorSettings

data class MkdAppSettings(
    val appUrls: List<String> = emptyList(),
    override val corSettings: MkdCorSettings = MkdCorSettings(),
    override val processor: MkdMeterProcessor = MkdMeterProcessor(corSettings),
): IMkdAppSettings

package com.fedorovsky.mkdservice.app.common

import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MkdCorSettings

interface IMkdAppSettings {
    val processor: MkdMeterProcessor
    val corSettings: MkdCorSettings
}
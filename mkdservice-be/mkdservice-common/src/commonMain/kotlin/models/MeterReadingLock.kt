package com.fedorovsky.mkdservice.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MeterReadingLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MeterReadingLock("")
    }
}
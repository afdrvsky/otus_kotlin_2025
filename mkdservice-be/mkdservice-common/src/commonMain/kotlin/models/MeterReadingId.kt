package com.fedorovsky.mkdservice.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MeterReadingId(private val id: Int) {
    fun asInt() = id

    companion object {
        val NONE = MeterReadingId(0)
    }
}
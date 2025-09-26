package com.fedorovsky.mkdservice.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MeterRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MeterRequestId("")
    }
}
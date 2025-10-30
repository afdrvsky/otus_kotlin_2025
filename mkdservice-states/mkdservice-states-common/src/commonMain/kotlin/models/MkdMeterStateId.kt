package com.fedorovsky.mkdservice.states.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MkdMeterStateId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = MkdMeterStateId("")
    }
}
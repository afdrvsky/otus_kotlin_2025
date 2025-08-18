package com.fedorovsky.mkdservice.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class MeterId(private val id: Int) {
    fun asInt() = id

    companion object {
        val NONE = MeterId(0)
    }
}
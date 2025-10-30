package com.fedorovsky.mkdservice.e2e.be

import com.fedorovsky.mkdservice.api.v1.models.MeterDebug
import com.fedorovsky.mkdservice.api.v1.models.MeterRequestDebugMode
import com.fedorovsky.mkdservice.e2e.be.base.BaseContainerTest
import com.fedorovsky.mkdservice.e2e.be.base.client.Client
import com.fedorovsky.mkdservice.e2e.be.base.client.RestClient
import com.fedorovsky.mkdservice.e2e.be.docker.KtorJvmCsDockerCompose
import com.fedorovsky.mkdservice.e2e.be.scenarios.v1.ScenariosV1
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestCsJvm: BaseContainerTest(KtorJvmCsDockerCompose) {
    private val client: Client = RestClient(compose)
    @Test
    fun info() {
        println("${this::class.simpleName}")
    }

    @Nested
    internal inner class V1: ScenariosV1(client, MeterDebug(mode = MeterRequestDebugMode.PROD))
}
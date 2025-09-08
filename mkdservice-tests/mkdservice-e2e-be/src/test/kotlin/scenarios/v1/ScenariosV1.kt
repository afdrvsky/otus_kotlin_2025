package com.fedorovsky.mkdservice.e2e.be.scenarios.v1

import com.fedorovsky.mkdservice.api.v1.models.MeterDebug
import com.fedorovsky.mkdservice.e2e.be.base.client.Client
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@Suppress("unused")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ScenariosV1(
    private val client: Client,
    private val debug: MeterDebug? = null
) {
    @Nested
    internal inner class CreateDeleteV1: ScenarioCreateDeleteV1(client, debug)

    @Nested
    internal inner class ReadV1: ScenarioReadV1(client, debug)

    @Nested
    internal inner class UpdateV1: ScenarioUpdateV1(client, debug)
}
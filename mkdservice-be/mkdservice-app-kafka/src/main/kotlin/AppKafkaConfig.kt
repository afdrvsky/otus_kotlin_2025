package com.fedorovsky.mkdservice.app.kafka

import com.fedorovsky.mkdservice.app.common.IMkdAppSettings
import com.fedorovsky.mkdservice.biz.MkdMeterProcessor
import com.fedorovsky.mkdservice.common.MkdCorSettings
import com.fedorovsky.mkdservice.logging.common.MkdLoggerProvider
import com.fedorovsky.mkdservice.logging.kermit.mkdLoggerKermit

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    override val corSettings: MkdCorSettings = MkdCorSettings(
        loggerProvider = MkdLoggerProvider { mkdLoggerKermit(it) }
    ),
    override val processor: MkdMeterProcessor = MkdMeterProcessor(corSettings),
): IMkdAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,; ]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "mkdservice" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "mkdservice-meter-v1-in" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "mkdservice-meter-v1-out" }
    }
}

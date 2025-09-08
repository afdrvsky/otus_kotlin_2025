package com.fedorovsky.mkdservice.app.kafka

import com.fedorovsky.mkdservice.common.MeterReadingContext

/**
 * Интерфейс стратегии для обслуживания версии API
 */
interface IConsumerStrategy {
    /**
     * Топики, для которых применяется стратегия
     */
    fun topics(config: AppKafkaConfig): InputOutputTopics
    /**
     * Сериализатор для версии API
     */
    fun serialize(source: MeterReadingContext): String
    /**
     * Десериализатор для версии API
     */
    fun deserialize(value: String, target: MeterReadingContext)
}

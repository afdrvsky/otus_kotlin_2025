package com.fedorovsky.mkdservice.app.kafka

import com.fedorovsky.mkdservice.api.v1.apiV1RequestDeserialize
import com.fedorovsky.mkdservice.api.v1.apiV1ResponseSerialize
import com.fedorovsky.mkdservice.api.v1.mappers.fromTransport
import com.fedorovsky.mkdservice.api.v1.mappers.toTransportMeter
import com.fedorovsky.mkdservice.api.v1.models.IRequest
import com.fedorovsky.mkdservice.api.v1.models.IResponse
import com.fedorovsky.mkdservice.common.MeterReadingContext

class ConsumerStrategyV1 : IConsumerStrategy {
    override fun topics(config: AppKafkaConfig): InputOutputTopics {
        return InputOutputTopics(config.kafkaTopicInV1, config.kafkaTopicOutV1)
    }

    override fun serialize(source: MeterReadingContext): String {
        val response: IResponse = source.toTransportMeter()
        return apiV1ResponseSerialize(response)
    }

    override fun deserialize(value: String, target: MeterReadingContext) {
        val request: IRequest = apiV1RequestDeserialize(value)
        target.fromTransport(request)
    }
}

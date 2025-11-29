import com.fedorovsky.mkdservice.api.v1.apiV1RequestSerialize
import com.fedorovsky.mkdservice.api.v1.apiV1ResponseDeserialize
import com.fedorovsky.mkdservice.api.v1.models.MeterCreateObject
import com.fedorovsky.mkdservice.api.v1.models.MeterCreateResponse
import com.fedorovsky.mkdservice.api.v1.models.MeterDebug
import com.fedorovsky.mkdservice.api.v1.models.MeterReadingCreateRequest
import com.fedorovsky.mkdservice.api.v1.models.MeterRequestDebugMode
import com.fedorovsky.mkdservice.api.v1.models.MeterRequestDebugStubs
import com.fedorovsky.mkdservice.app.kafka.AppKafkaConfig
import com.fedorovsky.mkdservice.app.kafka.AppKafkaConsumer
import com.fedorovsky.mkdservice.app.kafka.ConsumerStrategyV1
import com.fedorovsky.mkdservice.common.models.MeterReadingUnit
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.Test
import java.util.*
import kotlin.collections.first
import kotlin.collections.set
import kotlin.test.assertEquals


class KafkaControllerTest {
    @Test
    fun runKafka() {
        val consumer = MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)
        val producer = MockProducer(true, StringSerializer(), StringSerializer())
        val config = AppKafkaConfig()
        val inputTopic = config.kafkaTopicInV1
        val outputTopic = config.kafkaTopicOutV1

        val app = AppKafkaConsumer(config, listOf(ConsumerStrategyV1()), consumer = consumer, producer = producer)
        consumer.schedulePollTask {
            consumer.rebalance(Collections.singletonList(TopicPartition(inputTopic, 0)))
            consumer.addRecord(
                ConsumerRecord(
                    inputTopic,
                    PARTITION,
                    0L,
                    "test-1",
                    apiV1RequestSerialize(
                        MeterReadingCreateRequest(
                            meter = MeterCreateObject(
                                amount = "100.01",
                                unit = MeterReadingUnit.M3.toString(),
                                meterId = "1",
                                apartmentId = "10",
                            ),
                            debug = MeterDebug(
                                mode = MeterRequestDebugMode.STUB,
                                stub = MeterRequestDebugStubs.SUCCESS,
                            ),
                        ),
                    )
                )
            )
            app.close()
        }

        val startOffsets: MutableMap<TopicPartition, Long> = mutableMapOf()
        val tp = TopicPartition(inputTopic, PARTITION)
        startOffsets[tp] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        app.start()

        val message = producer.history().first()
        val result = apiV1ResponseDeserialize<MeterCreateResponse>(message.value())
        assertEquals(outputTopic, message.topic())
        assertEquals("170.08", result.meter?.amount)
    }

    companion object {
        const val PARTITION = 0
    }
}



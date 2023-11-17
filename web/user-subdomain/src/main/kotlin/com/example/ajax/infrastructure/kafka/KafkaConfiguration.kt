package com.example.ajax.infrastructure.kafka

import com.example.ajax.UserEvent
import com.example.ajax.infrastructure.configuration.kafka.CoreKafkaConfiguration
import com.pubsub.user.UserUpdatedEvent
import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kafka.receiver.KafkaReceiver
import reactor.kafka.sender.KafkaSender

@Configuration
class KafkaConfiguration(
    @Value("http://localhost:9092") private val bootstrapServers: String,
    @Value("http://localhost:8081") private val schemaRegistryUrl: String
) : CoreKafkaConfiguration(bootstrapServers, schemaRegistryUrl) {

    @Bean
    fun kafkaSenderUserUpdatedEvent(): KafkaSender<String, UserUpdatedEvent> =
        createKafkaSender(baseProducerProperties())

    @Bean
    fun kafkaReceiverUserUpdatedEvent(): KafkaReceiver<String, UserUpdatedEvent> {
        val customProperties: MutableMap<String, Any> = mutableMapOf(
            KafkaProtobufDeserializerConfig.SPECIFIC_PROTOBUF_VALUE_TYPE to UserUpdatedEvent::class.java.name
        )
        return createKafkaReceiver(
            baseConsumerProperties(customProperties),
            UserEvent.createUserEventKafkaTopic(UserEvent.UPDATED),
            "user-group"
        )
    }
}

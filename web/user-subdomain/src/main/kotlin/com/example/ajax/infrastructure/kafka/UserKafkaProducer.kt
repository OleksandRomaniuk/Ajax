package com.example.ajax.infrastructure.kafka


import com.example.ajax.UserEvent
import com.example.ajax.application.port.UserKafkaProducerOutPort
import com.example.ajax.domain.User
import com.example.ajax.infrastructure.mapper.UserMapper
import com.pubsub.user.UserUpdatedEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kafka.sender.KafkaSender

import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono

@Component
class UserKafkaProducer(
    private val kafkaSenderUserUpdatedEvent: KafkaSender<String, UserUpdatedEvent>,
    private val userMapper: UserMapper
) : UserKafkaProducerOutPort {

    override fun sendUserUpdatedEventToKafka(user: User): Mono<Unit> =
        Mono.fromSupplier { userMapper.mapToUserUpdatedEvent(userMapper.mapToProto(user)) }
            .flatMap {
                kafkaSenderUserUpdatedEvent.send(buildKafkaUpdatedMessage(it)).next()
            }
            .thenReturn(Unit)
            .doOnNext {
                logger.info("Sent event {} to topic {}", it, UserEvent.UPDATED)
            }

    override fun buildKafkaUpdatedMessage(event: UserUpdatedEvent) =
        SenderRecord.create(
            ProducerRecord(
                UserEvent.createUserEventKafkaTopic(UserEvent.UPDATED),
                event.user.id,
                event
            ),
            null
        ).toMono()

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserKafkaReceiver::class.java)
    }
}

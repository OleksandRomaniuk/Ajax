package com.example.ajaxproject.kafka

import com.example.ajax.User
import com.example.ajaxproject.UserEvent
import com.pubsub.user.UserDeletedEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono

@Component
class UserKafkaDeleteProducer(
    private val kafkaSenderUserDeletedEvent: KafkaSender<String, UserDeletedEvent>
) {
    fun sendUserDeletedEventToKafka(protoUser: User) {
        val userDeleteEvent = UserDeletedEvent.newBuilder().apply {
            user = protoUser
        }.build()
        val senderRecord = SenderRecord.create(
            ProducerRecord(
                UserEvent.UPDATED,
                protoUser.id,
                userDeleteEvent
            ),
            null
        )
        kafkaSenderUserDeletedEvent.send(senderRecord.toMono()).subscribe()
        logger.info("Sent event {} to topic {}", userDeleteEvent, UserEvent.DELETE)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserKafkaReceiver::class.java)
    }
}

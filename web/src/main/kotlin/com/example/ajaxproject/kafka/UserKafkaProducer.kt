package com.example.ajaxproject.kafka

import com.example.ajax.User
import com.example.ajaxproject.KafkaTopic
import com.pubsub.user.UserDeletedEvent
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Component
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono

@Component
class UserKafkaProducer(
    private val kafkaSenderUserDeleteEvent: KafkaSender<String, UserDeletedEvent>
) {
    fun sendUserDeleteEventToKafka(protoUser: User) {
        val userDeleteEvent = UserDeletedEvent.newBuilder().apply {
            user = protoUser
        }.build()
        val senderRecord = SenderRecord.create(
            ProducerRecord(
                KafkaTopic.User.DELETE,
                protoUser.id,
                userDeleteEvent
            ),
            null
        )
        kafkaSenderUserDeleteEvent.send(senderRecord.toMono()).subscribe()
    }
}

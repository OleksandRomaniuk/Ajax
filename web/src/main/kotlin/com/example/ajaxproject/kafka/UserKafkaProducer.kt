package com.example.ajaxproject.kafka

import com.example.ajaxproject.UserDeleteEvent
import com.example.ajaxproject.UserEvent
import com.example.ajaxproject.UserOuterClass.User
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.stereotype.Component
import reactor.kafka.sender.KafkaSender
import reactor.kafka.sender.SenderRecord
import reactor.kotlin.core.publisher.toMono

@Component
class UserKafkaProducer(
    private val kafkaSenderUserDeleteEvent: KafkaSender<String, UserDeleteEvent>
) {
    fun sendUserDeleteEventToKafka(protoUser: User) {
        val userDeleteEvent = UserDeleteEvent.newBuilder().apply {
            user = protoUser
        }.build()
        val senderRecord = SenderRecord.create(
            ProducerRecord(
                UserEvent.createUserEventKafkaTopic(UserEvent.DELETE),
                protoUser.id,
                userDeleteEvent
            ),
            null
        )
        kafkaSenderUserDeleteEvent.send(senderRecord.toMono()).subscribe()
    }
}

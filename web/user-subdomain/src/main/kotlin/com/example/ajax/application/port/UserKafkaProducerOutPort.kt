package com.example.ajax.application.port

import com.example.ajax.domain.User
import com.pubsub.user.UserUpdatedEvent
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderRecord

interface UserKafkaProducerOutPort {

    fun sendUserUpdatedEventToKafka(user: User): Mono<Unit>

    fun buildKafkaUpdatedMessage(event: UserUpdatedEvent): Mono<SenderRecord<String, UserUpdatedEvent, Nothing?>>
}

package com.example.ajaxproject.nats

import com.example.ajax.User
import com.google.protobuf.GeneratedMessageV3
import com.google.protobuf.Parser
import reactor.core.publisher.Flux

interface UserEventNatsService<EventT : GeneratedMessageV3> {

    val parser: Parser<EventT>

    fun subscribeToEvents(userId: String): Flux<EventT>

    fun publishEvent(updatedUser: User)
}

package com.example.ajax.infractucture.nats

import com.example.ajax.UserEvent
import com.example.ajax.infractucture.mongo.mapper.UserMapper
import com.example.ajax.infrastructure.adapters.nats.EventNatsService
import com.google.protobuf.Parser
import com.pubsub.user.UserUpdatedEvent
import io.nats.client.Connection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import com.example.ajax.User as ProtoUser

@Service
class UserUpdatedNatsServiceImpl(
    private val connection: Connection,
    private val userMapper: UserMapper
) : EventNatsService<UserUpdatedEvent> {

    override val parser: Parser<UserUpdatedEvent> = UserUpdatedEvent.parser()

    private val dispatcher = connection.createDispatcher()

    override fun subscribeToEvents(userId: String): Flux<UserUpdatedEvent> =
        Flux.create { sink ->
            dispatcher.apply {
                subscribe(UserEvent.createUserEventNatsSubject(userId, UserEvent.UPDATED))
                { message ->
                    val parsedData = parser.parseFrom(message.data)
                    sink.next(parsedData)
                    logger.info("Received event {} from subject {}", parsedData, message.subject)
                }
            }
        }

    override fun publishEvent(updatedUser: ProtoUser) {
        val updateEventSubject = UserEvent.createUserEventNatsSubject(updatedUser.id, UserEvent.UPDATED)
        val eventMessage = userMapper.mapToUserUpdatedEvent(updatedUser)
        connection.publish(updateEventSubject, eventMessage.toByteArray())
        logger.info("Published event {} to subject {}", eventMessage, updateEventSubject)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserUpdatedNatsServiceImpl::class.java)
    }
}

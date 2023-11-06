package com.example.ajaxproject.nats

import com.example.ajax.User
import com.example.ajaxproject.KafkaTopic
import com.google.protobuf.Parser
import com.pubsub.user.UserUpdatedEvent
import io.nats.client.Connection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class UserUpdatedNatsServiceImpl(
    private val connection: Connection
) : UserEventNatsService<UserUpdatedEvent> {

    override val parser: Parser<UserUpdatedEvent> = UserUpdatedEvent.parser()

    private val dispatcher = connection.createDispatcher()

    override fun subscribeToEvents(userId: String): Flux<UserUpdatedEvent> =
        Flux.create { sink ->
            dispatcher.apply {
                subscribe(KafkaTopic.User.NATS_UPDATE)
                { message ->
                    val parsedData = parser.parseFrom(message.data)
                    sink.next(parsedData)
                    logger.info("Received event {} from subject {}", parsedData, message.subject)
                }
            }
        }

    override fun publishEvent(updatedUser: User) {
        val updateEventSubject = KafkaTopic.User.NATS_UPDATE
        val eventMessage = updatedUser.mapToUserUpdatedEvent()
        connection.publish(updateEventSubject, eventMessage.toByteArray())
        logger.info("Published event {} to subject {}", eventMessage, updateEventSubject)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserUpdatedNatsServiceImpl::class.java)
    }
}

package com.example.ajaxproject.kafka

import com.example.ajaxproject.nats.UserEventNatsService
import com.pubsub.user.UserUpdatedEvent
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers
import reactor.kafka.receiver.KafkaReceiver

@Component
class UserKafkaReceiver(
    private val userKafkaConsumer: KafkaReceiver<String, UserUpdatedEvent>,
    private val userEventNatsService: UserEventNatsService<UserUpdatedEvent>
) {

    @PostConstruct
    fun initialize() {
        userKafkaConsumer.receiveAutoAck()
            .flatMap { fluxRecord ->
                fluxRecord
                    .map {
                        userEventNatsService.publishEvent(it.value().user)
                    }
                    .doOnNext {
                        logger.info("Received event {}", it)
                    }
            }
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserKafkaReceiver::class.java)
    }
}

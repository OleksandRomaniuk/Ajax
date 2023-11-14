package com.example.ajax.infrastructure.nats

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

@Configuration
class NatsConfiguration {

    @Bean
    fun natsConnection(@Value("\${nats.url}") natsUrl: String): Connection = Nats.connect(natsUrl)

    @Bean
    fun handleMessageScheduler(): Scheduler {
        return Schedulers.boundedElastic()
    }
}


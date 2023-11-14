package com.example.ajax.infrastructure.redis.repository

import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infrastructure.configuration.redis.CoreRedisConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate

@Configuration
class RedisConfiguration : CoreRedisConfiguration<PrivateChatRoom>(clazz = PrivateChatRoom::class.java) {
    @Bean
    fun reactiveRedisTemplate(
        connectionFactory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, PrivateChatRoom> = createReactiveRedisTemplate(clazz, connectionFactory)
}

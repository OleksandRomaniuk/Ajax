package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.RedisPrivateChatRoomRepository
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RedisPrivateRoomRepositoryImpl(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, PrivateChatRoom>,
) : RedisPrivateChatRoomRepository {

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        return reactiveRedisTemplate.opsForValue()
            .set(privateChatRoom.id, privateChatRoom)
            .thenReturn(privateChatRoom)
    }

    override fun findById(chatRoomId: String): Mono<PrivateChatRoom> {
        return reactiveRedisTemplate.opsForValue().get(chatRoomId)
    }
}

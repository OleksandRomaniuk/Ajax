package com.example.ajaxproject.repository.redis

import com.example.ajaxproject.domain.PrivateChatRoom
import com.example.ajaxproject.domain.redis.toDomain
import com.example.ajaxproject.domain.toRedis
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class PrivateChatRoomRedisRepositoryImpl(
    private val redisTemplate: ReactiveRedisTemplate<String, PrivateChatRoom>,
) : PrivateChatRoomRedisRepository {

    override fun findById(id: String): Mono<PrivateChatRoom> {
        return redisTemplate.opsForValue().get(id)
    }

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        val redisRoom = privateChatRoom.toRedis()
        return redisTemplate.opsForValue()
            .set(redisRoom.id, privateChatRoom)
            .thenReturn(redisRoom.toDomain())
    }
}

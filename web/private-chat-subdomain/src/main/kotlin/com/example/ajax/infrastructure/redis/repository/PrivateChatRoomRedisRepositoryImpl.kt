package com.example.ajax.infrastructure.redis.repository

import com.example.ajax.application.port.PrivateChatRoomRepositoryOutPort
import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infrastructure.mapper.PrivateChatRoomMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository("redisPrivateRoomRepository")
class PrivateChatRoomRedisRepositoryImpl(
    private val redisTemplate: ReactiveRedisTemplate<String, PrivateChatRoom>,
    private val privateChatRoomMapper: PrivateChatRoomMapper
) : PrivateChatRoomRepositoryOutPort {

    override fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom> {
        return redisTemplate.opsForValue().get(chatRoomId)
    }

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        val redisRoom = privateChatRoomMapper.mapToRedis(privateChatRoom)
        return redisTemplate.opsForValue()
            .set(redisRoom.id, privateChatRoom)
            .thenReturn(privateChatRoomMapper.fromRedisToDomain(redisRoom))
    }
}

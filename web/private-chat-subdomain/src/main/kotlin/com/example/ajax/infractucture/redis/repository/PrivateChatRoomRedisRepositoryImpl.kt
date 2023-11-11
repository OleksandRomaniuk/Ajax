package com.example.ajax.infractucture.redis.repository

import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infractucture.mapper.PrivateChatRoomMapper
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class PrivateChatRoomRedisRepositoryImpl(
    private val redisTemplate: ReactiveRedisTemplate<String, PrivateChatRoom>,
    private val privateChatRoomMapper: PrivateChatRoomMapper
) : PrivateChatRoomRedisRepository {

    override fun findById(id: String): Mono<PrivateChatRoom> {
        return redisTemplate.opsForValue().get(id)
    }

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        val redisRoom = privateChatRoomMapper.mapToRedis(privateChatRoom)
        return redisTemplate.opsForValue()
            .set(redisRoom.id, privateChatRoom)
            .thenReturn(privateChatRoomMapper.fromRedisToDomain(redisRoom))
    }
}

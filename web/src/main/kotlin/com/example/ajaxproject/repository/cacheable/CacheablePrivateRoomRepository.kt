package com.example.ajaxproject.repository.cacheable

import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import com.example.ajaxproject.repository.redis.PrivateChatRoomRedisRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class CacheablePrivateRoomRepository<T>(
    @Qualifier("mongoPrivateRoomRepository") private val privateChatRoomRepository: PrivateChatRoomRepository,
    private val privateChatRoomRedisRepository: PrivateChatRoomRedisRepository
) : CacheableRepository<T> {

    override fun save(entity: T): Mono<T> {
        if (entity is PrivateChatRoom) {
            val room = entity as PrivateChatRoom
            return privateChatRoomRepository.save(room)
                .flatMap { privateChatRoomRedisRepository.save(it) }
                .thenReturn(entity)
        }
        return Mono.error(IllegalArgumentException("Invalid entity type"))
    }

    override fun findById(id: String): Mono<T> {
        return privateChatRoomRedisRepository.findById(id)
            .switchIfEmpty(
                privateChatRoomRepository.findPrivateChatRoomById(id)
                    .flatMap { privateChatRoomRedisRepository.save(it) }
                    .switchIfEmpty(
                        Mono.error(NoSuchElementException("Can't get entity by id $id"))
                    ))
            .map { it as T }
    }
}

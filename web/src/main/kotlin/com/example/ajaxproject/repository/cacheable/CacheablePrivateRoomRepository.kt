package com.example.ajaxproject.repository.cacheable

import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import com.example.ajaxproject.repository.redis.PrivateChatRoomRedisRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class CacheablePrivateRoomRepository(
    @Qualifier("mongoPrivateRoomRepository") private val privateChatRoomRepository: PrivateChatRoomRepository,
    private val privateChatRoomRedisRepository: PrivateChatRoomRedisRepository
) : PrivateChatRoomRepository {

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        return privateChatRoomRepository.save(privateChatRoom).flatMap { privateChatRoomRedisRepository.save(it) }
    }

    override fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom> {
        return privateChatRoomRedisRepository.findById(chatRoomId)
            .switchIfEmpty(
                privateChatRoomRepository.findPrivateChatRoomById(chatRoomId)
                    .flatMap { privateChatRoomRedisRepository.save(it) }
                    .switchIfEmpty(
                        Mono.error(NoSuchElementException("Can't get room by id $chatRoomId"))
                    ))
    }
}

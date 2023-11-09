package com.example.ajaxproject.repository.cacheable

import com.example.ajaxproject.domain.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import com.example.ajaxproject.repository.redis.PrivateChatRoomRedisRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class CacheablePrivateRoomRepository(
    @Qualifier("mongoPrivateRoomRepository") private val privateChatRoomRepository: PrivateChatRoomRepository,
    private val privateChatRoomRedisRepository: PrivateChatRoomRedisRepository
) : CacheableRepository<PrivateChatRoom> {

    override fun save(entity: PrivateChatRoom): Mono<PrivateChatRoom> {
        return privateChatRoomRepository.save(entity)
                .flatMap { privateChatRoomRedisRepository.save(it) }
    }

    override fun findById(id: String): Mono<PrivateChatRoom> {
        return privateChatRoomRedisRepository.findById(id)
            .switchIfEmpty(
                privateChatRoomRepository.findPrivateChatRoomById(id)
                    .flatMap { privateChatRoomRedisRepository.save(it) }
                    .switchIfEmpty(
                        Mono.error(NoSuchElementException("Can't get User by id $id"))
                    )
            )
    }
}

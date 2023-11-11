package com.example.ajax.infractucture.cacheable

import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infractucture.mongo.repository.PrivateChatRoomRepository
import com.example.ajax.infractucture.redis.repository.PrivateChatRoomRedisRepository
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

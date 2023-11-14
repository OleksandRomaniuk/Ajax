package com.example.ajax.infrastructure.cacheable

import com.example.ajax.application.port.CacheableRepository
import com.example.ajax.application.port.PrivateChatRoomRedisRepositoryOutPort
import com.example.ajax.application.port.PrivateChatRoomRepositoryOutPort
import com.example.ajax.domain.PrivateChatRoom
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class CacheablePrivateRoomRepository(
    @Qualifier("mongoPrivateRoomRepository") private val privateChatRoomRepositoryOutPort:
    PrivateChatRoomRepositoryOutPort,
    private val privateChatRoomRedisRepositoryOutPort: PrivateChatRoomRedisRepositoryOutPort
) : CacheableRepository<PrivateChatRoom> {

    override fun save(entity: PrivateChatRoom): Mono<PrivateChatRoom> {
        return privateChatRoomRepositoryOutPort.save(entity)
            .flatMap { privateChatRoomRedisRepositoryOutPort.save(it) }
    }

    override fun findById(id: String): Mono<PrivateChatRoom> {
        return privateChatRoomRedisRepositoryOutPort.findById(id)
            .switchIfEmpty(
                privateChatRoomRepositoryOutPort.findPrivateChatRoomById(id)
                    .flatMap { privateChatRoomRedisRepositoryOutPort.save(it) }
                    .switchIfEmpty(
                        Mono.error(NoSuchElementException("Can't get User by id $id"))
                    )
            )
    }
}

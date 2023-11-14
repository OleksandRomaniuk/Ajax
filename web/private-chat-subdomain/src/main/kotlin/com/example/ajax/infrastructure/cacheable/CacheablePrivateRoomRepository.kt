package com.example.ajax.infrastructure.cacheable

import com.example.ajax.application.port.PrivateChatRoomRepositoryOutPort
import com.example.ajax.domain.PrivateChatRoom
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Primary
@Repository
class CacheablePrivateRoomRepository(
    @Qualifier("mongoPrivateRoomRepository")
    private val privateChatRoomRepositoryOutPort: PrivateChatRoomRepositoryOutPort,
    @Qualifier("redisPrivateRoomRepository")
    private val privateChatRoomRedisRepositoryOutPort: PrivateChatRoomRepositoryOutPort
) : PrivateChatRoomRepositoryOutPort {

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        return privateChatRoomRepositoryOutPort.save(privateChatRoom)
            .flatMap { privateChatRoomRedisRepositoryOutPort.save(it) }
    }

    override fun findPrivateChatRoomById(chatRoomId: String): Mono<PrivateChatRoom> {
        return privateChatRoomRedisRepositoryOutPort.findPrivateChatRoomById(chatRoomId)
            .switchIfEmpty {
                privateChatRoomRepositoryOutPort.findPrivateChatRoomById(chatRoomId)
                    .flatMap { privateChatRoomRedisRepositoryOutPort.save(it) }
                    .switchIfEmpty {
                        Mono.error(NoSuchElementException("Can't get User by id $chatRoomId"))
                    }
            }
    }
}

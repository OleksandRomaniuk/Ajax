package com.example.ajax.application.service

import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infractucture.cacheable.CacheableRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
class PrivateChatRoomOutPort(
    private val privateChatRoomRepository: CacheableRepository<PrivateChatRoom>,
    private val userServiceOutPort: UserServiceOutPort
) : PrivateChatRoomService {

    override fun createPrivateRoom(senderId: String, recipientId: String): Mono<PrivateChatRoom> {

        val roomId = roomIdFormat(senderId, recipientId)
        return privateChatRoomRepository.findById(roomId)
            .switchIfEmpty(Mono.zip(userServiceOutPort.getById(senderId), userServiceOutPort.getById(recipientId))
                .flatMap { (sender, recipient) ->
                    val newRoom = PrivateChatRoom(
                        id = roomId, senderId = sender.id, recipientId = recipient.id
                    )
                    privateChatRoomRepository.save(newRoom)
                })
    }

    override fun getPrivateRoom(roomId: String): Mono<PrivateChatRoom> {
        return privateChatRoomRepository.findById(roomId)
            .switchIfEmpty(Mono.error(NoSuchElementException("Can't get room by id $roomId")))
    }

    private fun roomIdFormat(id1: String, id2: String): String {
        val smallerId = minOf(id1, id2)
        val higherId = maxOf(id1, id2)
        return "$smallerId-$higherId"
    }
}

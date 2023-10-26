package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.dto.request.RoomDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatMessageRepository
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import com.example.ajaxproject.service.interfaces.PrivateChatService
import com.example.ajaxproject.service.interfaces.UserService
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2


@Service
class PrivateChatServiceImpl(
    private val privateChatRoomRepository: PrivateChatRoomRepository,
    private val privateChatMessageRepository: PrivateChatMessageRepository,
    private val userService: UserService
) : PrivateChatService {

    override fun createPrivateRoom(senderId: String, recipientId: String): Mono<PrivateChatRoom> {

        val roomId = roomIdFormat(senderId, recipientId)
        return privateChatRoomRepository.findChatRoomById(roomId)
            .switchIfEmpty(
                Mono.zip(
                    userService.getById(senderId),
                    userService.getById(recipientId)
                ).flatMap { (sender, recipient) ->
                    privateChatRoomRepository.save(
                        PrivateChatRoom(
                            id = roomId,
                            senderId = sender.id,
                            recipientId = recipient.id
                        )
                    )
                }
            )
    }


    override fun getPrivateRoom(roomId: String): Mono<PrivateChatRoom> {
        return privateChatRoomRepository.findChatRoomById(roomId)
            .switchIfEmpty(Mono.error(NotFoundException("Room with $roomId not found")))
    }

    override fun sendPrivateMessage(privateMessageDTO: PrivateMessageDTO): Mono<PrivateChatMessage> {
        return createPrivateRoom(privateMessageDTO.senderId, privateMessageDTO.recipientId)
            .flatMap { room ->
                val privateChatMessage = PrivateChatMessage(
                    id = ObjectId(),
                    privateChatRoomId = room.id,
                    message = privateMessageDTO.message,
                    senderId = privateMessageDTO.senderId
                )
                privateChatMessageRepository.save(privateChatMessage)
                    .doOnSuccess { logger.info("Send private message {}", it.id) }
            }
    }

    override fun getAllPrivateMessages(roomDTO: RoomDTO): Flux<PrivateChatMessage> {
        return privateChatMessageRepository.findAllByPrivateChatRoomId(
            roomIdFormat(roomDTO.senderId, roomDTO.recipientId)
        )
            .switchIfEmpty(
                Flux.error { NotFoundException("Message between users not found") }

            )
    }

    private fun roomIdFormat(id1: String, id2: String): String {
        val smallerId = minOf(id1, id2)
        val higherId = maxOf(id1, id2)
        return "$smallerId-$higherId"
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PrivateChatServiceImpl::class.java)
    }
}

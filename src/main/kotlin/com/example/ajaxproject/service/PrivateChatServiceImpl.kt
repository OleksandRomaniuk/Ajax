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

@Service
class PrivateChatServiceImpl(
    private val privateChatRoomRepository: PrivateChatRoomRepository,
    private val privateChatMessageRepository: PrivateChatMessageRepository,
    private val userService: UserService,
) : PrivateChatService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GroupChatServiceImpl::class.java)
    }

    override fun createPrivateRoom(senderId: String, recipientId: String): PrivateChatRoom {

        userService.getUserById(senderId)

        userService.getUserById(recipientId)

        val roomId = roomIdFormat(senderId, recipientId)

        logger.info("Create new private room {}", roomId)

        return privateChatRoomRepository.findChatRoomById(roomId)
            ?: privateChatRoomRepository.save(
                PrivateChatRoom(
                    id = roomId,
                    senderId = senderId,
                    recipientId = recipientId
                )
            )
    }

    fun roomIdFormat(id1: String, id2: String): String {
        val smallerId = minOf(id1, id2)
        val higherId = maxOf(id1, id2)
        return "$smallerId-$higherId"
    }

    override fun getPrivateRoom(roomId: String): PrivateChatRoom? {
        return privateChatRoomRepository.findChatRoomById(roomId) ?:
            throw NotFoundException("Room with $roomId not fount")
    }

    override fun sendPrivateMessage(privateMessageDTO: PrivateMessageDTO): PrivateChatMessage {
        val privateChatMessage = PrivateChatMessage(
            id = ObjectId(),
            privateChatRoom = createPrivateRoom(privateMessageDTO.senderId, privateMessageDTO.recipientId),
            message = privateMessageDTO.message,
            senderId = privateMessageDTO.senderId
        )
        privateChatMessageRepository.save(privateChatMessage)

        logger.info("Send private message {}", privateChatMessage.id)

        return privateChatMessage
    }

    override fun getAllPrivateMessages(roomDTO: RoomDTO): List<PrivateChatMessage> {

        userService.getUserById(roomDTO.senderId)
        userService.getUserById(roomDTO.recipientId)

        val listOfMessage = privateChatMessageRepository.findAllByPrivateChatRoomId(
            roomIdFormat(roomDTO.senderId, roomDTO.recipientId)
        )

        if (listOfMessage.isEmpty()) {
            logger.warn("Empty chat list")
            throw NotFoundException("Message between users not found")
        }

        logger.info("List of all messages for chat")

        return listOfMessage
    }
}

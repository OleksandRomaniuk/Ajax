package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.RoomDTO
import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatMessageRepository
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.PrivateChatService
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PrivateChatServiceImpl @Autowired constructor(
    private val privateChatRoomRepository: PrivateChatRoomRepository,
    private val privateChatMessageRepository: PrivateChatMessageRepository,
    private val userRepository: UserRepository,
) : PrivateChatService {

    private val logger: Logger = LoggerFactory.getLogger(GroupChatServiceImpl::class.java)

    override fun createPrivateRoom(senderId: Long, recipientId: Long): PrivateChatRoom {

        // If I had used authentication, this check would not have happened
        userRepository.findById(senderId).orElseThrow { NotFoundException("Sender not found") }

        userRepository.findById(recipientId).orElseThrow { NotFoundException("Recipient not found") }

        val roomId = roomIdFormat(senderId, recipientId)

        logger.info("Create new private room {$roomId}")

        return privateChatRoomRepository.findById(roomId)
            .orElseGet {
                privateChatRoomRepository.save(
                    PrivateChatRoom(
                        id = roomId,
                        senderId = senderId,
                        recipientId = recipientId)
                )
            }
    }

    fun roomIdFormat(id1: Long, id2: Long): String {
        val smallerId = minOf(id1, id2)
        val higherId = maxOf(id1, id2)
        return "$smallerId-$higherId"
    }

    override fun getPrivateRoom(roomId: String): PrivateChatRoom? {
        return privateChatRoomRepository.findById(roomId).
            orElseThrow{NotFoundException("Room ith ID $roomId not found")}
    }

    override fun sendPrivateMessage(privateMessageDTO: PrivateMessageDTO): PrivateChatMessage {
        val privateChatMessage = PrivateChatMessage(
            id = ObjectId(),
            privateChatRoom = createPrivateRoom(privateMessageDTO.senderId, privateMessageDTO.recipientId),
            message = privateMessageDTO.message,
            sender = userRepository.findById(privateMessageDTO.senderId).get(),
        )
        privateChatMessageRepository.save(privateChatMessage)

        logger.info("Send private message {${privateChatMessage.id}}")

        return privateChatMessage
    }

    override fun getAllPrivateMessages(roomDTO: RoomDTO): List<PrivateChatMessage> {
        userRepository.findById(roomDTO.senderId).orElseThrow { NotFoundException("Sender not found") }
        userRepository.findById(roomDTO.recipientId).orElseThrow { NotFoundException("Recipient not found") }

        val listOfMessage = privateChatMessageRepository.findAllByPrivateChatRoomId(
            roomIdFormat(roomDTO.senderId, roomDTO.recipientId))

        if (listOfMessage.isEmpty()) {
            logger.warn("Empty chat list")
            throw NotFoundException("Message between users not found")
        }

        logger.info("List of all messages for chat")

        return listOfMessage
    }
}

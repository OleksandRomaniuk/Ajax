package com.example.ajaxproject.service

import com.example.ajaxproject.dto.RoomDTO
import com.example.ajaxproject.dto.SendMessageDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.ChatMessage
import com.example.ajaxproject.model.ChatRoom
import com.example.ajaxproject.repository.ChatMessageRepository
import com.example.ajaxproject.repository.ChatRoomRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.ChatService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatServiceImpl @Autowired constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate
) : ChatService {

    override fun createRoom(senderId: Long, recipientId: Long): ChatRoom {

        // If I had used authentication, this check would not have happened
        userRepository.findById(senderId).orElseThrow { NotFoundException("Sender not found") }

        userRepository.findById(recipientId).orElseThrow { NotFoundException("Recipient not found") }

        val roomId = roomIdFormat(senderId, recipientId)

        return chatRoomRepository.findById(roomId)
            .orElseGet {
                chatRoomRepository.save(
                    ChatRoom(id = roomId, senderId = senderId, recipientId = recipientId)
                )
            }
    }

    fun roomIdFormat(id1: Long, id2: Long): String {
        val smallerId = minOf(id1, id2)
        val higherId = maxOf(id1, id2)
        return "$smallerId-$higherId"
    }

    override fun getRoom(roomId: String): ChatRoom {

        val query = Query()

        query.addCriteria(Criteria.where("_id").`is`(roomId))

        return mongoTemplate.findOne(query, ChatRoom::class.java)
            ?: throw NotFoundException("Room ith ID $roomId not found")
    }

    override fun sendMessage(sendMessageDTO: SendMessageDTO): ChatMessage {
        val chatMessage = ChatMessage(
            id = ObjectId(),
            chatRoom = createRoom(sendMessageDTO.senderId, sendMessageDTO.recipientId),
            message = sendMessageDTO.message,
            timeStamp = LocalDateTime.now()
        )
        chatMessageRepository.save(chatMessage)
        return chatMessage
    }

    override fun getAllMessages(roomDTO: RoomDTO): List<ChatMessage> {
        userRepository.findById(roomDTO.senderId).orElseThrow { NotFoundException("Sender not found") }
        userRepository.findById(roomDTO.recipientId).orElseThrow { NotFoundException("Recipient not found") }

        val roomId = roomIdFormat(roomDTO.senderId, roomDTO.recipientId)

        val query = Query()

        query.addCriteria(Criteria.where("chatRoom.id").`is`(roomId))

        val listOfMessage = mongoTemplate.find(query, ChatMessage::class.java)

        if (listOfMessage.isEmpty()) throw NotFoundException("Message between users not found")

        return listOfMessage
    }
}

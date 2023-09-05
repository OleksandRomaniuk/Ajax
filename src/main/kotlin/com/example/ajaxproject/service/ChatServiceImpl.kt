package com.example.ajaxproject.service

import com.example.ajaxproject.dto.RoomDTO
import com.example.ajaxproject.dto.SendMessageDTO
import com.example.ajaxproject.exeption.NoSuchElementException
import com.example.ajaxproject.exeption.UserNotFoundException
import com.example.ajaxproject.model.ChatMessage
import com.example.ajaxproject.model.ChatRoom
import com.example.ajaxproject.repository.ChatMessageRepository
import com.example.ajaxproject.repository.ChatRoomRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.ChatService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatServiceImpl @Autowired constructor(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val userRepository: UserRepository,
) : ChatService {

    override fun createRoom(senderId: Long, recipientId: Long): ChatRoom {

        //if I had used authentication, this check would not have happened
        userRepository.findById(senderId).orElseThrow { UserNotFoundException("Sender not found") }

        userRepository.findById(recipientId).orElseThrow { UserNotFoundException("Recipient not found") }

        val sortedUserIds = listOf(senderId, recipientId).sorted()

        val roomId = "${sortedUserIds[0]}-${sortedUserIds[1]}"

        return chatRoomRepository.findById(roomId)
            .orElseGet {
                chatRoomRepository.save(
                    ChatRoom(id = roomId, senderId = senderId, recipientId = recipientId)
                )
            }
    }


    override fun getRoom(roomId: String): ChatRoom {
        return chatRoomRepository.findById(roomId)
            .orElseThrow { NoSuchElementException("Chat room with ID $roomId not found") }
    }

    override fun sendMessage(sendMessageDTO: SendMessageDTO): ChatMessage {
        val chatMessage = ChatMessage(
                                      id = ObjectId(),
                                      chatRoom =createRoom(sendMessageDTO.senderId , sendMessageDTO.recipientId),
                                      message = sendMessageDTO.message,
                                      timeStamp =LocalDateTime.now())
        chatMessageRepository.save(chatMessage)
        return chatMessage
    }

    override fun getAllMessages(roomDTO: RoomDTO): List<ChatMessage> {

        userRepository.findById(roomDTO.senderId).orElseThrow { UserNotFoundException("Sender not found") }

        userRepository.findById(roomDTO.recipientId).orElseThrow { UserNotFoundException("Recipient not found") }

            val sortedUserIds = listOf(roomDTO.senderId, roomDTO.recipientId).sorted()

            val roomId = "${sortedUserIds[0]}-${sortedUserIds[1]}"

            val listOfMessage = chatMessageRepository.findAllByChatRoomId(roomId)

            if (listOfMessage.isEmpty()) throw NoSuchElementException("Message between users not found")

            return listOfMessage
        }


}

package com.example.ajaxproject.service

import com.example.ajaxproject.dto.CreateChatDto
import com.example.ajaxproject.dto.GroupChatDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.exeption.WrongActionException
import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatMessageRepository
import com.example.ajaxproject.repository.GroupChatRoomRepository
import com.example.ajaxproject.repository.PrivateChatMessageRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.GroupChatService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Service

@Service
class GroupChatServiceImpl @Autowired constructor(
    private val groupChatRoomRepository: GroupChatRoomRepository,
    private val userRepository: UserRepository,
    private val groupChatMessageRepository: GroupChatMessageRepository
) : GroupChatService {

    override fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom {

        val user = userRepository.findById(createChatDto.adminId).orElseThrow { NotFoundException("User not found") }

        val groupChatRoom = GroupChatRoom(
            id = ObjectId(),
            chatName = createChatDto.chatName,
            adminId = createChatDto.adminId,
            chatMembers = listOf(user)
        )

        ArrayList(groupChatRoom.chatMembers).add(user)

        return groupChatRoomRepository.save(groupChatRoom)
    }

    override fun getAllChatMembers(chatId: String): List<User> {

        val chat = groupChatRoomRepository.findChatRoom(chatId)
            ?: throw NotFoundException("Chat with ID $chatId not found")

        return chat.chatMembers
    }

    override fun addUserToChat(chatId: String, userId: Long): List<User> {

        val chat = groupChatRoomRepository.findChatRoom(chatId)

        val user = userRepository.findById(userId).orElseThrow { NotFoundException("Sender not found") }

        val updatedChatMembers = chat?.chatMembers?.let { ArrayList(it) }

        updatedChatMembers?.add(user)

        chat?.chatMembers = updatedChatMembers!!

        return groupChatRoomRepository.save(chat).chatMembers

    }
    override fun sendMessageToGroup(groupChatDto: GroupChatDTO): GroupChatMessage {

        val chatMessage = GroupChatMessage(
            id = ObjectId(),
            groupChatRoom = groupChatRoomRepository.findChatRoom(groupChatDto.chatId)!!,
            sender = userRepository.findById(groupChatDto.senderId).get(),
            message = groupChatDto.message,
        )

        return groupChatMessageRepository.save(chatMessage)

    }

    override fun getAllGroupMessages(chatId: String): List<GroupChatMessage> {
       return groupChatMessageRepository.findAll()
    }

    override fun leaveGroupChat(chatId: String, userId: Long): Boolean {

        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User not found") }

        val chat = groupChatRoomRepository.findChatRoom(chatId)

        if (chat?.adminId !=user.id){

            val updatedChatMembers = chat?.chatMembers?.filterNot { it.id == user.id }

            chat?.chatMembers = updatedChatMembers!!

            groupChatRoomRepository.save(chat)

            return true

        }else{
            throw WrongActionException("Admin cant leave a chat")
        }
    }

}

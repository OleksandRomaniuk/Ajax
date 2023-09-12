package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDTO
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.exeption.WrongActionException
import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatMessageRepository
import com.example.ajaxproject.repository.GroupChatRoomRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.GroupChatService
import com.example.ajaxproject.service.mapper.GroupChatMessageMapper
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GroupChatServiceImpl @Autowired constructor(
    private val groupChatRoomRepository: GroupChatRoomRepository,
    private val userRepository: UserRepository,
    private val groupChatMessageRepository: GroupChatMessageRepository,
    private val groupChatMessageMapper: GroupChatMessageMapper
) : GroupChatService {

    private val logger: Logger = LoggerFactory.getLogger(GroupChatServiceImpl::class.java)

    override fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom {

        val user = userRepository.findById(createChatDto.adminId).orElseThrow {
            logger.error("Error creating group chat room")
            NotFoundException("User not found")
        }

        val groupChatRoom = GroupChatRoom(
            id = ObjectId(),
            chatName = createChatDto.chatName,
            adminId = createChatDto.adminId,
            chatMembers = listOf(user)
        )

        ArrayList(groupChatRoom.chatMembers).add(user)

        logger.info("Group chat room created successfully")

        return groupChatRoomRepository.save(groupChatRoom)
    }

    override fun getAllChatMembers(chatId: String): List<User> {

        val chat = groupChatRoomRepository.findChatRoom(chatId)
            ?: throw NotFoundException("Chat with ID $chatId not found")

        logger.info("Retrieved chat members for chat ID: $chatId")

        return chat.chatMembers
    }

    override fun addUserToChat(chatId: String, userId: Long): List<User> {

        val chat = groupChatRoomRepository.findChatRoom(chatId)

        val user = userRepository.findById(userId).orElseThrow { NotFoundException("Sender not found") }

        val updatedChatMembers = chat?.chatMembers?.let { ArrayList(it) }

        updatedChatMembers?.add(user)

        chat?.chatMembers = updatedChatMembers!!

        logger.info("User with ID {$userId} added to chat ID {$chatId}")

        return groupChatRoomRepository.save(chat).chatMembers

    }
    override fun sendMessageToGroup(groupChatDto: GroupChatDTO): GroupChatMessageResponse {

        val chatMessage = GroupChatMessage(
            id = ObjectId(),
            groupChatRoom = groupChatRoomRepository.findChatRoom(groupChatDto.chatId)!!,
            sender = userRepository.findById(groupChatDto.senderId).get(),
            message = groupChatDto.message,
        )

        logger.info("Message sent to group chat ID: {$groupChatDto.chatId}")

        val responseDto = groupChatMessageMapper.toResponseDto(chatMessage)

        return responseDto

    }

    override fun getAllGroupMessages(chatId: String): List<GroupChatMessageResponse> {
        val messages = groupChatMessageRepository.findAll()
        return messages.map { groupChatMessageMapper.toResponseDto(it) }
    }

    override fun leaveGroupChat(chatId: String, userId: Long): Boolean {

        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User not found") }

        val chat = groupChatRoomRepository.findChatRoom(chatId)

        if (chat?.adminId !=user.id){

            val updatedChatMembers = chat?.chatMembers?.filterNot { it.id == user.id }

            chat?.chatMembers = updatedChatMembers!!

            groupChatRoomRepository.save(chat)

            logger.info("User with ID {$userId} left chat ID {$chatId}")

            return true

        }else{
            logger.warn("Admin attempted to leave chat ID {$chatId}")
            throw WrongActionException("Admin cant leave a chat")
        }
    }

}

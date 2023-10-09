package com.example.ajaxproject.service

import com.example.ajaxproject.config.Notification
import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.dto.responce.OffsetPaginateResponse
import com.example.ajaxproject.exeption.WrongActionException
import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatMessageRepository
import com.example.ajaxproject.repository.GroupChatRoomRepository
import com.example.ajaxproject.service.interfaces.GroupChatService
import com.example.ajaxproject.service.interfaces.UserService
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupChatServiceImpl (
    private val groupChatRoomRepository: GroupChatRoomRepository,
    private val userService: UserService,
    private val groupChatMessageRepository: GroupChatMessageRepository
) : GroupChatService {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GroupChatServiceImpl::class.java)
    }

    override fun createGroupRoom(createChatDto: CreateChatDto): GroupChatRoom {

        val user = userService.getUserById(createChatDto.adminId)

        val groupChatRoom = GroupChatRoom(
            id = ObjectId().toHexString(),
            chatName = createChatDto.chatName,
            adminId = user.id,
            chatMembers = listOf(user)
        )

        groupChatRoom.chatMembers + user

        groupChatRoomRepository.save(groupChatRoom)

        logger.info("Group chat room created successfully")

        return groupChatRoom

    }

    override fun getAllChatMembers(chatId: String): List<User> {

        val chatMembers = groupChatRoomRepository.findChatRoom(chatId).chatMembers

        logger.info("Retrieved chat members for chat ID: {}" , chatId)

        return chatMembers
    }

    override fun addUserToChat(chatId: String, userId: String): List<User> {

        val chat = groupChatRoomRepository.findChatRoom(chatId)
        val user = userService.getUserById(userId)

        val existingUser = chat.chatMembers.find { it.id == userId }

        if (existingUser != null) {
            logger.info("User with ID {} is already present in chat ID {}", userId, chatId)
            throw WrongActionException("User is already present in this chat")
        }

        chat.chatMembers += user
        logger.info("User with ID {} added to chat ID {}", userId, chatId)

        val updatedChatRoom = groupChatRoomRepository.save(chat)

        return updatedChatRoom.chatMembers

    }

    @Notification
    override fun sendMessageToGroup(groupChatDto: GroupChatDto): GroupChatMessageResponse {

        val chatMessage = GroupChatMessage(
            id = ObjectId().toHexString(),
            groupChatRoom = groupChatDto.chatId,
            senderId = groupChatDto.senderId,
            message = groupChatDto.message,
            date = Date()
        )

        logger.info("Message sent to group chat ID: {}" , groupChatDto.chatId)

        groupChatMessageRepository.save(chatMessage)

        return toResponseDto(chatMessage)

    }

    override fun getAllGroupMessages(chatId: String): List<GroupChatMessageResponse> {

        val messages = groupChatMessageRepository.findAllMessagesInChat(chatId)

        return messages.map { toResponseDto(it) }
    }

    override fun leaveGroupChat(chatId: String, userId: String): Boolean {

        val user = userService.getUserById(userId)

        val chat = groupChatRoomRepository.findChatRoom(chatId)

        if (chat.adminId != user.id) {

            chat.chatMembers = chat.chatMembers.filterNot { it.id == user.id }

            groupChatRoomRepository.save(chat)

            logger.info("User with ID {} left chat ID {}" ,userId ,chatId)

            return true

        } else {

            logger.warn("Admin attempted to leave chat ID {}" , chatId)

            throw WrongActionException("Admin cant leave a chat")
        }
    }

    fun toResponseDto(chatMessage: GroupChatMessage): GroupChatMessageResponse {
        return GroupChatMessageResponse(
            id = chatMessage.id,
            senderId = chatMessage.senderId,
            roomId = chatMessage.groupChatRoom,
            message = chatMessage.message,
            date = chatMessage.date
        )
    }

    override fun getMessagesByChatRoomIdWithPagination(chatRoomId: String, limit:Int, offset:Int)
            : OffsetPaginateResponse {
                 val messagesByOffsetPagination = groupChatMessageRepository
                        .findMessagesByChatRoomIdWithPagination(chatRoomId, offset, offset)
                            return OffsetPaginateResponse(
                                messagesByOffsetPagination.first, messagesByOffsetPagination.second)
    }



}

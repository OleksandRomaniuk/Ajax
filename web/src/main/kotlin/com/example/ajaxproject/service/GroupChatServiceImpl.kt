package com.example.ajaxproject.service

import com.example.ajaxproject.config.Notification
import com.example.ajaxproject.dto.request.CreateChatDTO
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
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
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
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

    override fun createGroupRoom(createChatDto: CreateChatDTO): Mono<GroupChatRoom> {
        return userService.getById(createChatDto.adminId)
            .flatMap { user ->
                val groupChatRoom = GroupChatRoom(
                    id = ObjectId().toHexString(),
                    chatName = createChatDto.chatName,
                    adminId = user.id,
                    chatMembers = listOf(user)
                )
                groupChatRoomRepository.save(groupChatRoom)
                    .doOnSuccess { logger.info("Group chat room created successfully") }
            }
    }

    override fun getAllChatMembers(chatId: String): Mono<List<User>> {
        return groupChatRoomRepository.findChatRoom(chatId)
            .flatMapMany { chatRoom ->
                Flux.fromIterable(chatRoom.chatMembers)
            }
            .collectList()
            .doOnSuccess { _ -> logger.info("Retrieved chat members for chat ID: $chatId") }
    }


    override fun addUserToChat(chatId: String, userId: String): Mono<User> {
        return groupChatRoomRepository.findChatRoom(chatId)
            .flatMap { chat ->
                userService.getById(userId)
                    .flatMap { user ->
                        val existingUser = chat.chatMembers.find { it.id == userId }
                        if (existingUser != null) {
                            logger.info("User with ID $userId is already present in chat ID $chatId")
                            Mono.just(existingUser)
                        } else {
                            chat.chatMembers += user
                            logger.info("User with ID $userId added to chat ID $chatId")
                            groupChatRoomRepository.save(chat).thenReturn(user)
                        }
                    }
            }
    }

    @Notification
    override fun sendMessageToGroup(groupChatDto: GroupChatDto): Mono<GroupChatMessageResponse> {
        val chatMessage = GroupChatMessage(
            id = ObjectId().toHexString(),
            groupChatRoom = groupChatDto.chatId,
            senderId = groupChatDto.senderId,
            message = groupChatDto.message,
            date = Date()
        )
        return groupChatMessageRepository.save(chatMessage)
            .doOnSuccess { logger.info("Message sent to group chat ID: ${groupChatDto.chatId}") }
            .map { toResponseDto(it) }
    }

    @Cacheable("getAllGroupMessages")
    override fun getAllGroupMessages(chatId: String): Mono<List<GroupChatMessageResponse>> {
        return groupChatMessageRepository.findAllMessagesInChat(chatId)
            .map { toResponseDto(it) }
            .collectList()
            .cache()
    }


    override fun leaveGroupChat(userId: String , chatId: String): Mono<Boolean> {
        return Mono.zip(
            groupChatRoomRepository.findChatRoom(chatId),
            userService.getById(userId)
        ).flatMap {
            val chat = it.t1
            val user = it.t2
            if (chat.adminId != user.id) {
                chat.chatMembers = chat.chatMembers.filterNot { it.id == user.id }
                groupChatRoomRepository.save(chat)
                    .doOnSuccess { logger.info("User with ID $userId left chat ID $chatId") }
                    .thenReturn(true)
            } else {
                logger.warn("Admin attempted to leave chat ID $chatId")
                Mono.error<Boolean?>(WrongActionException("Admin can't leave a chat")).thenReturn(false)
            }
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
}


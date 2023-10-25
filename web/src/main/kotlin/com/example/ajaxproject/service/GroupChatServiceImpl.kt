package com.example.ajaxproject.service

import com.example.ajaxproject.config.Notification
import com.example.ajaxproject.dto.request.CreateChatDTO
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.exeption.NotFoundException
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
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.util.*

@Service
class GroupChatServiceImpl (
    private val groupChatRoomRepository: GroupChatRoomRepository,
    private val userService: UserService,
    private val groupChatMessageRepository: GroupChatMessageRepository
) : GroupChatService {

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

    override fun getAllChatMembers(chatId: String): Flux<User> {
        return groupChatRoomRepository.findChatRoom(chatId)
            .flatMapMany { chatRoom ->
                Flux.fromIterable(chatRoom.chatMembers)
            }
    }

    override fun addUserToChat(chatId: String, userId: String): Mono<User> {
        return Mono.zip(
            groupChatRoomRepository.findChatRoom(chatId),
            userService.getById(userId)
        ).flatMap { (chat, user) ->
            val existingUser = chat.chatMembers.find { it.id == userId }
            if (existingUser != null) {
                logger.info("User with ID {} is already present in chat ID {}", userId, chatId)
                Mono.just(existingUser)
            } else {
                chat.chatMembers += user
                logger.info("User with ID {} added to chat with ID {}", userId, chatId)
                groupChatRoomRepository.save(chat).thenReturn(user)
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
            .doOnSuccess { logger.info("Message sent to group chat ID: {}", groupChatDto.chatId) }
            .map { toResponseDto(it) }
            .cache()
    }

    @Cacheable("getAllGroupMessages")
    override fun getAllGroupMessages(chatId: String): Mono<List<GroupChatMessageResponse>> {
        return groupChatMessageRepository.findAllMessagesInChat(chatId)
            .map { toResponseDto(it) }
            .collectList()
    }

    fun findChatRoom(id: String): Mono<GroupChatRoom> {
        return groupChatRoomRepository.findChatRoom(id)
            .switchIfEmpty(Mono.error(NotFoundException("ChatRoom not found")))
    }

    override fun leaveGroupChat(userId: String, chatId: String): Mono<String> {
        return Mono.zip(
            findChatRoom(chatId),
            userService.getById(userId)
        ).flatMap { (chat, user) ->
            if (chat.adminId == user.id) {
                logger.warn("Admin attempted to leave chat ID {}", chatId)
                Mono.just("Admin cannot leave chat")
            } else {
                chat.chatMembers = chat.chatMembers.filterNot { it.id == user.id }
                groupChatRoomRepository.save(chat)
                    .map { "User left chat" }
                    .onErrorResume { error ->
                        Mono.just("Failed to leave chat: ${error.message}")
                    }
            }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GroupChatServiceImpl::class.java)
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

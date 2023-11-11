package com.example.ajax.application.service

import com.example.ajax.domain.GroupChatRoom
import com.example.ajax.domain.User
import com.example.ajax.exception.NotFoundException
import com.example.ajax.exception.WrongActionException
import com.example.ajax.infractucture.mongo.repository.MongoGroupChatRoomRepositoryInPort
import com.example.ajax.infractucture.rest.dto.CreateChatRequest
import org.bson.types.ObjectId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
class GroupChatService(
    private val mongoGroupChatRoomRepositoryInPort: MongoGroupChatRoomRepositoryInPort,
    private val userServiceOutPort: UserServiceOutPort,
) : GroupChatServiceOutPort {

    override fun createGroupRoom(createChatRequest: CreateChatRequest): Mono<GroupChatRoom> {
        return userServiceOutPort.getById(createChatRequest.adminId)
            .flatMap { user ->
                val groupChatRoom = GroupChatRoom(
                    id = ObjectId().toHexString(),
                    chatName = createChatRequest.chatName,
                    adminId = user.id,
                    chatMembers = listOf(user)
                )
                mongoGroupChatRoomRepositoryInPort.save(groupChatRoom)
                    .doOnSuccess { logger.info("Group chat room created successfully") }
            }
    }

    override fun getAllChatMembers(chatId: String): Flux<User> {
        return mongoGroupChatRoomRepositoryInPort.findChatRoom(chatId)
            .flatMapMany { chatRoom ->
                Flux.fromIterable(chatRoom.chatMembers)
            }
    }

    override fun addUserToChat(chatId: String, userId: String): Mono<GroupChatRoom> {
        return Mono.zip(
            findChatRoom(chatId),
            userServiceOutPort.getById(userId)
        )
            .filter { (chat, _) -> chat.chatMembers.none { it.id == userId } }
            .flatMap { (chat, user) ->
                chat.chatMembers += user
                logger.info("User with ID {} added to chat with ID {}", userId, chatId)
                mongoGroupChatRoomRepositoryInPort.save(chat)
            }
            .switchIfEmpty(Mono.error(WrongActionException("User is already present in chat")))
    }

    fun findChatRoom(id: String): Mono<GroupChatRoom> {
        return mongoGroupChatRoomRepositoryInPort.findChatRoom(id)
            .switchIfEmpty(Mono.error(NotFoundException("ChatRoom not found")))
    }

    override fun leaveGroupChat(userId: String, chatId: String): Mono<String> {
        return Mono.zip(
            findChatRoom(chatId),
            userServiceOutPort.getById(userId)
        ).flatMap { (chat, user) ->
            if (chat.adminId == user.id) {
                logger.warn("Admin attempted to leave chat ID {}", chatId)
                Mono.just("Admin cannot leave chat")
            } else {
                chat.chatMembers = chat.chatMembers.filterNot { it.id == user.id }
                mongoGroupChatRoomRepositoryInPort.save(chat)
                    .map { "User left chat" }
                    .onErrorResume { error ->
                        Mono.just("Failed to leave chat: ${error.message}")
                    }
            }
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GroupChatService::class.java)
    }
}

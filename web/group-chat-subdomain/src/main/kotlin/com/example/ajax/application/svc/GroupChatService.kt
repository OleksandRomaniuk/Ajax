package com.example.ajax.application.svc

import com.example.ajax.application.port.UserServiceInPort
import com.example.ajax.application.port.GroupChatRoomRepositoryOutPort
import com.example.ajax.application.port.GroupChatServiceInPort
import com.example.ajax.domain.GroupChatRoom
import com.example.ajax.domain.User
import com.example.ajax.domain.exception.NotFoundException
import com.example.ajax.domain.exception.WrongActionException
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
    private val groupChatRoomRepositoryOutPort: GroupChatRoomRepositoryOutPort,
    private val userServiceInPort: UserServiceInPort,
) : GroupChatServiceInPort {

    override fun createGroupRoom(chatName: String, adminId: String): Mono<GroupChatRoom> {
        return userServiceInPort.getById(adminId)
            .flatMap { user ->
                val groupChatRoom = GroupChatRoom(
                    id = ObjectId().toHexString(),
                    chatName = chatName,
                    adminId = user.id,
                    chatMembers = listOf(user)
                )
                groupChatRoomRepositoryOutPort.save(groupChatRoom)
                    .doOnSuccess { logger.info("Group chat room created successfully") }
            }
    }

    override fun getAllChatMembers(chatId: String): Flux<User> {
        return groupChatRoomRepositoryOutPort.findChatRoom(chatId)
            .flatMapMany { chatRoom ->
                Flux.fromIterable(chatRoom.chatMembers)
            }
    }

    override fun addUserToChat(chatId: String, userId: String): Mono<GroupChatRoom> {
        return Mono.zip(
            findChatRoom(chatId),
            userServiceInPort.getById(userId)
        )
            .filter { (chat, _) -> chat.chatMembers.none { it.id == userId } }
            .flatMap { (chat, user) ->
                chat.chatMembers += user
                logger.info("User with ID {} added to chat with ID {}", userId, chatId)
                groupChatRoomRepositoryOutPort.save(chat)
            }
            .switchIfEmpty(Mono.error(WrongActionException("User is already present in chat")))
    }

    fun findChatRoom(id: String): Mono<GroupChatRoom> {
        return groupChatRoomRepositoryOutPort.findChatRoom(id)
            .switchIfEmpty(Mono.error(NotFoundException("ChatRoom not found")))
    }

    override fun leaveGroupChat(userId: String, chatId: String): Mono<String> {
        return Mono.zip(
            findChatRoom(chatId),
            userServiceInPort.getById(userId)
        ).flatMap { (chat, user) ->
            if (chat.adminId == user.id) {
                logger.warn("Admin attempted to leave chat ID {}", chatId)
                Mono.just("Admin cannot leave chat")
            } else {
                chat.chatMembers = chat.chatMembers.filterNot { it.id == user.id }
                groupChatRoomRepositoryOutPort.save(chat)
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

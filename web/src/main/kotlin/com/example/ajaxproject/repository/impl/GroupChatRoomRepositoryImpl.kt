package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatRoomRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class GroupChatRoomRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : GroupChatRoomRepository {

    override fun findChatRoom(chatId: String): Mono<GroupChatRoom> {
        val chatQuery = Query.query(Criteria.where("_id").`is`(chatId))
        return reactiveMongoTemplate.findOne(chatQuery, GroupChatRoom::class.java)
            .flatMap { chatRoom ->
                validateAndCleanChatMembers(chatId)
                    .then(Mono.just(chatRoom))
            }
    }

    override fun save(chat: GroupChatRoom): Mono<GroupChatRoom> {
        return reactiveMongoTemplate.save(chat)
    }

    override fun validateAndCleanChatMembers(chatRoomId: String): Mono<Void> {
        val query = Query.query(Criteria.where("_id").`is`(chatRoomId))
        return reactiveMongoTemplate.findOne(query, GroupChatRoom::class.java)
            .flatMap { chatRoom ->
                val validationMono = Flux.fromIterable(chatRoom.chatMembers)
                    .flatMap { user ->
                        isValidUser(user.id)
                            .map { isValid -> Pair(user, isValid) }
                    }
                    .collectList()
                    .flatMap { userValidationPairs ->
                        val validUserIds = userValidationPairs.filter { it.second }.map { it.first.id }
                        if (validUserIds != chatRoom.chatMembers.map { it.id }) {
                            val update = Update().set("chatMembers", validUserIds)
                            reactiveMongoTemplate.updateFirst(query, update, GroupChatRoom::class.java)
                        } else {
                            Mono.empty()
                        }
                    }

                validationMono.then()
            }
    }

    private fun isValidUser(userId: String): Mono<Boolean> {
        return reactiveMongoTemplate.exists(Query.query(Criteria.where("_id").`is`(userId)), User::class.java)
    }
}


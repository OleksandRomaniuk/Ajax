package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.repository.GroupChatMessageRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class GroupChatMessageRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : GroupChatMessageRepository {

    override fun save(chatMessage: GroupChatMessage): Mono<GroupChatMessage> {
        return reactiveMongoTemplate.save(chatMessage)
    }

    override fun findAllMessagesInChat(groupChatRoomId: String): Flux<GroupChatMessage> {
        val query = Criteria.where("groupChatRoomId").`is`(groupChatRoomId)
        return reactiveMongoTemplate.find(Query.query(query), GroupChatMessage::class.java)
    }
}


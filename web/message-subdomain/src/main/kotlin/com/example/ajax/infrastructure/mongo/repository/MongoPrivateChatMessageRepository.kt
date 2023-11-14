package com.example.ajax.infrastructure.mongo.repository

import com.example.ajax.application.port.ChatMessageRepositoryOutPort
import com.example.ajax.domain.ChatMessage
import com.example.ajax.infrastructure.mapper.ChatMessageMapper
import com.example.ajax.infrastructure.mongo.entity.ChatMessageEntity
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class MongoPrivateChatMessageRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val privateChatMessageMapper: ChatMessageMapper
) : ChatMessageRepositoryOutPort {

    override fun findAllByChatRoomId(chatRoomId: String): Flux<ChatMessage> {
        val query = Query(Criteria.where("сhatRoomId").`is`(chatRoomId))
        return reactiveMongoTemplate.find(query, ChatMessageEntity::class.java)
            .map { privateChatMessageMapper.mapToDomain(it) }

    }

    override fun save(privateChatMessage: ChatMessage): Mono<ChatMessage> =
        reactiveMongoTemplate.save(privateChatMessageMapper.mapToEntity(privateChatMessage))
            .map { privateChatMessageMapper.mapToDomain(it) }
}

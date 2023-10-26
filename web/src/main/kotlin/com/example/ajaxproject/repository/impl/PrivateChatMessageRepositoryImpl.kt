package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.repository.PrivateChatMessageRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class PrivateChatMessageRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : PrivateChatMessageRepository {

    override fun findAllByPrivateChatRoomId(chatRoomId: String): Flux<PrivateChatMessage> {
        val query = Query(Criteria.where("privateChatRoomId").`is`(chatRoomId))
        return reactiveMongoTemplate.find(query, PrivateChatMessage::class.java)
    }

    override fun save(privateChatMessage: PrivateChatMessage): Mono<PrivateChatMessage> =
        reactiveMongoTemplate.save(privateChatMessage)
}

package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class PrivateChatRoomRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : PrivateChatRoomRepository {

    override fun findChatRoomById(roomId: String): Mono<PrivateChatRoom> {
        val query = Query(Criteria.where("_id").`is`(roomId))
        return reactiveMongoTemplate.findOne(query, PrivateChatRoom::class.java)
    }

    override fun save(privateChatRoom: PrivateChatRoom): Mono<PrivateChatRoom> {
        return reactiveMongoTemplate.save(privateChatRoom)
    }

}

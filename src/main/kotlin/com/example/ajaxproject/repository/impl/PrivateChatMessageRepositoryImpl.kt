package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.repository.PrivateChatMessageRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class PrivateChatMessageRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : PrivateChatMessageRepository {

    override fun findAllByPrivateChatRoomId(chatRoomId: String): List<PrivateChatMessage> {
        val query = Query(Criteria.where("privateChatRoom.chatRoomId").`is`(chatRoomId))
        return mongoTemplate.find(query, PrivateChatMessage::class.java)
    }

    override fun save(privateChatMessage: PrivateChatMessage): PrivateChatMessage =
        mongoTemplate.save(privateChatMessage)
}

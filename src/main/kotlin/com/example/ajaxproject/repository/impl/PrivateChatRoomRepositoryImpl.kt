package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.repository.PrivateChatRoomRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class PrivateChatRoomRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : PrivateChatRoomRepository {

    override fun findChatRoomById(roomId: String): PrivateChatRoom? {
        val query = Query(Criteria.where("_id").`is`(roomId))
        return mongoTemplate.findOne(query, PrivateChatRoom::class.java)
    }

    override fun save(privateChatRoom: PrivateChatRoom): PrivateChatRoom = mongoTemplate.save(privateChatRoom)
}

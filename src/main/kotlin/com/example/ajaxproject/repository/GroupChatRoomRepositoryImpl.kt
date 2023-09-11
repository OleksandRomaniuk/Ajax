package com.example.ajaxproject.repository

import com.example.ajaxproject.model.GroupChatRoom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class GroupChatRoomRepositoryImpl @Autowired constructor(
    private val mongoTemplate: MongoTemplate
) : CustomRoomRepository {

    override fun findChatRoom(chatId: String): GroupChatRoom? {
        val chatQuery = Query.query(Criteria.where("_id").`is`(chatId))
        return mongoTemplate.findOne(chatQuery, GroupChatRoom::class.java)
    }

}

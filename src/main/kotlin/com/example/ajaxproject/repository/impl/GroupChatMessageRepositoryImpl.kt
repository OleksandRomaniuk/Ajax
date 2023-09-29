package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatMessage
import com.example.ajaxproject.repository.GroupChatMessageRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class GroupChatMessageRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : GroupChatMessageRepository {

    override fun save(chatMessage: GroupChatMessage): GroupChatMessage = mongoTemplate.save(chatMessage)

    override fun findAllMessagesInChat(groupChatRoomId: String): List<GroupChatMessage> {
        val query = Query.query(Criteria.where("groupChatRoomId").`is`(groupChatRoomId))
        return mongoTemplate.find(query, GroupChatMessage::class.java)
    }
}

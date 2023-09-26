package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatRoomRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository

@Repository
class GroupChatRoomRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : GroupChatRoomRepository {

    override fun findChatRoom(chatId: String): GroupChatRoom {
        val chatQuery = Query.query(Criteria.where("_id").`is`(chatId))
        validateAndCleanChatMembers(chatId)
        return mongoTemplate.findOne(chatQuery, GroupChatRoom::class.java)!!
    }

    override fun save(chat: GroupChatRoom): GroupChatRoom = mongoTemplate.save(chat)

    override fun validateAndCleanChatMembers(chatRoomId: String) {

        val query = Query.query(Criteria.where("_id").`is`(chatRoomId))
        val chatRoom = mongoTemplate.findOne(query, GroupChatRoom::class.java) ?: return

        val validUserIds = chatRoom.chatMembers.filter { isValidUser(it.id) }

        if (validUserIds.size != chatRoom.chatMembers.size) {
            val update = Update().set("chatMembers", validUserIds)
            mongoTemplate.updateFirst(query, update, GroupChatRoom::class.java)
        }
    }

    private fun isValidUser(userId: String): Boolean {
        return mongoTemplate.exists(Query.query(Criteria.where("_id").`is`(userId)), User::class.java)
    }
}

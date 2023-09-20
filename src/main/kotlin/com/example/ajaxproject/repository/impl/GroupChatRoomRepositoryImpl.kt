package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatRoomRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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

        val chatRoom =
            mongoTemplate.findOne(Query.query(Criteria.where("_id").`is`(chatRoomId)), GroupChatRoom::class.java)
                ?: return

        val validUserIds = chatRoom.chatMembers.filter { isValidUser(it.id) }

        if (validUserIds.size != chatRoom.chatMembers.size) {

            chatRoom.chatMembers = validUserIds

            mongoTemplate.save(chatRoom)
        }
    }

    private fun isValidUser(userId: String): Boolean {
        return mongoTemplate.exists(Query.query(Criteria.where("_id").`is`(userId)), User::class.java)
    }
}

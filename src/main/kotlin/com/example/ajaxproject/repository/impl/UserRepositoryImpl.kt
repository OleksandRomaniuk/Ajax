package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : UserRepository {

    override fun findUserById(userId: String): User {
        val userQuery = Query.query(Criteria.where("_id").`is`(userId))
        return mongoTemplate.findOne(userQuery, User::class.java)!!
    }

    override fun findAll(): List<User> {
        return mongoTemplate.findAll(User::class.java)
    }

    override fun deleteById(userId: String) {
        validateAndCleanChatMembersForUser(userId)
        mongoTemplate.findAndRemove(Query.query(Criteria.where("_id").`is`(userId)), User::class.java)
    }

    fun validateAndCleanChatMembersForUser(userIdToRemove: String) {

        val chatRooms = mongoTemplate.find(
            Query.query(Criteria.where("chatMembers._id").`is`(userIdToRemove)),
            GroupChatRoom::class.java
        )

        chatRooms.forEach { chatRoom ->
            chatRoom.chatMembers = chatRoom.chatMembers.toMutableList().apply {
                removeIf { it.id == userIdToRemove }
            }
            mongoTemplate.save(chatRoom)
        }
    }

    override fun save(user: User): User = mongoTemplate.save(user)

    override fun findByEmail(email: String): User? {
        val userQuery = Query.query(Criteria.where("email").`is`(email))
        return mongoTemplate.findOne(userQuery, User::class.java)
    }
}

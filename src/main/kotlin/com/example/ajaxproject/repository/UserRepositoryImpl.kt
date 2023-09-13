package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : CustomUserRepository {

    override fun findUser(userId: String): User {
        val userQuery = Query.query(Criteria.where("_id").`is`(userId))
        return mongoTemplate.findOne(userQuery, User::class.java)!!
    }

}

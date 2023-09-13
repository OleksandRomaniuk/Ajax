package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : MongoRepository<User, String>{
    fun findUserById(userId: String) : User
}

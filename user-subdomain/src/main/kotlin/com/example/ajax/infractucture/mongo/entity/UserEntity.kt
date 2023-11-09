package com.example.ajax.infractucture.mongo.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class UserEntity(

    @Id
    var userId: String,
    val email: String,
    var password: String,
)

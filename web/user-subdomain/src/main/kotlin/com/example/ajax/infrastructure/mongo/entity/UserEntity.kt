package com.example.ajax.infrastructure.mongo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class UserEntity(

    @Id
    var userId: String,
    val email: String,
    var password: String,
)

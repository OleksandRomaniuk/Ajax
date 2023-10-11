package com.example.ajaxproject.dto.request


import com.example.ajaxproject.User
import org.bson.types.ObjectId

data class UserRequest(
    val email: String,
    val password: String,
)
fun UserRequest.toUser(): com.example.ajaxproject.model.User{
    return com.example.ajaxproject.model.User(
        id = ObjectId().toHexString(),
        email = email,
        password = password,
    )
}



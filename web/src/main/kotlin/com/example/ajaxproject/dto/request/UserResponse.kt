package com.example.ajaxproject.dto.request


import com.example.ajaxproject.User

data class UserResponse(
    val id: String,
    val email: String,
    val password: String,
)

fun User.toUserResponse(): UserResponse {
    return UserResponse(
        id = id,
        email = email,
        password = password,
        )
}
fun UserResponse.toUser(): com.example.ajaxproject.model.User{
    return com.example.ajaxproject.model.User(
        id = id,
        email = email,
        password = password,
    )
}

fun UserResponse.toProtoUser(): User {
    return User.newBuilder()
        .setId(id)
        .setEmail(email)
        .setPassword(password)
        .build()
}

package com.example.ajaxproject.dto.request


import com.example.ajaxproject.User

data class UserDTO(
    val id: String,
    val email: String,
    val password: String,
)

fun User.toUserRequest(): UserDTO {
    return UserDTO(
        id = id,
        email = email,
        password = password,
        )
}
fun UserDTO.toUser(): com.example.ajaxproject.model.User{
    return com.example.ajaxproject.model.User(
        id = id,
        email = email,
        password = password,
    )
}

fun UserDTO.toProtoUser(): User {
    return User.newBuilder()
        .setId(id)
        .setEmail(email)
        .setPassword(password)
        .build()
}

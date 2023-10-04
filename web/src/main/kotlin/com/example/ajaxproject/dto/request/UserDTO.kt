package com.example.ajaxproject.dto.request


import com.example.ajaxproject.User

data class UserDTO(
    val id: String,
    val email: String,
    val password: String,
)
fun User.toUserRequest(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        password = this.password,
    )
}
fun UserDTO.toProtoUser(): User {
    return User.newBuilder()
        .setId(this.id)
        .setEmail(this.email)
        .setPassword(this.password)
        .build()
}

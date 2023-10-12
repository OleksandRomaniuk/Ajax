package com.example.ajaxproject.model

import com.example.ajaxproject.dto.request.UserDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id
    val id: String,
    val email: String = "",
    val password: String = "",
)

fun User.toUserDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        password = this.password
    )
}


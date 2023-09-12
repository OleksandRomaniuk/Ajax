package com.example.ajaxproject.service.mapper

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toEntity(userDTO: UserDTO): User {
        return User(
            email = userDTO.email,
            password = userDTO.password,
        )
    }
}
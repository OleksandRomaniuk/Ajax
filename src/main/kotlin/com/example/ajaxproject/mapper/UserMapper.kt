package com.example.ajaxproject.mapper

import com.example.ajaxproject.dto.UserDTO
import com.example.ajaxproject.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toEntity(userDTO: UserDTO): User {
        return User(
            firstName = userDTO.firstName,
            secondName = userDTO.secondName,
            email = userDTO.email,
            password = userDTO.password,
            role = userDTO.role
        )
    }
}

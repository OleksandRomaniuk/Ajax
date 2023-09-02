package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.UserDTO
import com.example.ajaxproject.model.User

interface UserService {

    fun createUser(userDTO: UserDTO): User

    fun updateUser(id: Long, userDTO: UserDTO): User

    fun deleteUser(id: Long)

    fun findUserById(id: Long): User?

    fun findAllUsers(): List<User>

}
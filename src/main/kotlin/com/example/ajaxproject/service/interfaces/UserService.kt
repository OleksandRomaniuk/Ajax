package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.model.User

interface UserService {

    fun createUser(userDTO: UserDTO): User

    fun updateUser(id: Long, userDTO: UserDTO): User

    fun deleteUser(id: Long)

    fun getUserById(id: Long): User?

    fun getAllUsers(): List<User>

}

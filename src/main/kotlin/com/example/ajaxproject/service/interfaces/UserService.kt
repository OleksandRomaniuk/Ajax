package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.model.User

interface UserService {

    fun createUser(userDTO: UserDTO): User

    fun updateUser(id: String, userDTO: UserDTO): User

    fun deleteUser(id: String)

    fun getUserById(id: String): User

    fun getAllUsers(): List<User>
}

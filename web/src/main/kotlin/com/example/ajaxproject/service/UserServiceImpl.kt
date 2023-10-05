package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.User
import com.example.ajaxproject.model.toUserDTO
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun createUser(userDTO: UserDTO): UserDTO {
        val user = toEntity(userDTO)
        return userRepository.save(user).toUserDTO()
    }

    fun toEntity(userDTO: UserDTO): User {
        return User(
            id = userDTO.id,
            email = userDTO.email,
            password = userDTO.password,
        )
    }

    override fun updateUser(id: String, userDTO: UserDTO): User {
        getUserById(id).copy(
            id = id,
            email = userDTO.email,
            password = userDTO.password,
        ).also {
            userRepository.save(it)
            return it
        }
    }

    override fun deleteUser(id: String) {
        return userRepository.deleteById(id)
    }

    override fun getUserById(id: String): User {
        return userRepository.findUserById(id) ?: throw NotFoundException("User id not found")

    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}
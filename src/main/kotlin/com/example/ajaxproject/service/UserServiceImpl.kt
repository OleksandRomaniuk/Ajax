package com.example.ajaxproject.service

import com.example.ajaxproject.dto.UserDTO
import com.example.ajaxproject.mapper.UserMapper
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {
    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun findById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun createUser(userDTO: UserDTO): User {
        val user = userMapper.toEntity(userDTO)
        return userRepository.save(user)
    }

    fun updateUser(id: Long, userDTO: UserDTO): User {
        val existingUser = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        val updatedUser = userMapper.toEntity(userDTO)
        updatedUser.id = existingUser.id
        return userRepository.save(updatedUser)
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}

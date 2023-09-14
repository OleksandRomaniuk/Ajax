package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import com.example.ajaxproject.service.mapper.UserMapper
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun createUser(userDTO: UserDTO): User {
        val user = userMapper.toEntity(userDTO)
        return userRepository.save(user)
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
        return userRepository.findById(id).orElseThrow { NotFoundException("User not found") }

    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }
}

package com.example.ajaxproject.service

import com.example.ajaxproject.dto.UserDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.mapper.UserMapper
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserService {

    override fun createUser(userDTO: UserDTO): User {
        val user = userMapper.toEntity(userDTO)
        return userRepository.save(user)
    }

    override fun updateUser(id: Long, userDTO: UserDTO): User {
        val user = userRepository.findById(id)
            .orElseThrow { NotFoundException("User not found") }

        user.apply {
            email = userDTO.email
            password = userDTO.password
        }

        return userRepository.save(user)
    }

    override fun deleteUser(id: Long) {
        return userRepository.deleteById(id)
    }

    override fun getUserById(id: Long): User? {
        return userRepository.findById(id)
            .orElseThrow { NotFoundException("User not found") }
    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

}

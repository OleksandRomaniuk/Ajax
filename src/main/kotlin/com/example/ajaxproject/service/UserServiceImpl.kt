package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import com.example.ajaxproject.service.mapper.UserMapper
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

    override fun updateUser(id: String, userDTO: UserDTO): User {
        val user = userRepository.findById(id)

        if (user.isPresent) {
            val existingUser = user.get()

            userDTO.email.let { existingUser.email = it }
            userDTO.password.let { existingUser.password = it }

            return userRepository.save(existingUser)
        } else {
            throw NotFoundException("User with ID $id not found")
        }
    }

    override fun deleteUser(id: String) {
        return userRepository.deleteById(id)
    }

    override fun getUserById(id: String): User? {
        return userRepository.findUser(id)

    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

}

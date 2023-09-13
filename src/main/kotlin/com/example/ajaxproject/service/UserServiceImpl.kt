package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import com.example.ajaxproject.service.mapper.UserMapper
import org.bson.types.ObjectId
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
        val user = userRepository.findUser(id)

        user.apply {
            email = userDTO.email
            password = userDTO.password
        }

        return userRepository.save(user)
    }

    override fun deleteUser(id: String) {
        return userRepository.deleteById(ObjectId(id))
    }

    override fun getUserById(id: String): User? {
        return userRepository.findUser(id)

    }

    override fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

}

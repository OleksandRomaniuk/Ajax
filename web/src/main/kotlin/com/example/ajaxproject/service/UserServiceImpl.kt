package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.UserResponse
import com.example.ajaxproject.dto.request.UserRequest
import com.example.ajaxproject.dto.request.toUser
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.exeption.WrongActionException
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun create(userRequest: UserRequest): Mono<User> {
        return userRepository.save(userRequest.toUser())
            .onErrorMap(WrongActionException::class.java) {
                WrongActionException("Duplicate user error")
            }
    }

    override fun updateUser(userResponse: UserResponse): Mono<User> {
        return userRepository.findById(userResponse.id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
            .flatMap { existingUser ->
                val updatedUser = existingUser.copy(
                    email = userResponse.email,
                    password = userResponse.password
                )
                userRepository.save(updatedUser)
            }
    }

    override fun deleteUser(id: String): Mono<DeleteResult> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
            .flatMap {
                userRepository.deleteById(id)
            }
    }

    override fun getAll(page: Int, size: Int): Flux<User> =
        userRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<User> {
        return userRepository
            .findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
    }
}

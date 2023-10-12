package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.dto.request.UserRequest
import com.example.ajaxproject.model.User
import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {

    fun updateUser(userResponse: UserDTO): Mono<User>

    fun deleteUser(id: String): Mono<DeleteResult>

    fun getAll(page: Int, size: Int): Flux<User>

    fun getById(id: String): Mono<User>

    fun save(userDTO: UserDTO): Mono<User>

    fun create(userRequest: UserRequest): Mono<User>
}

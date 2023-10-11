package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.UserResponse
import com.example.ajaxproject.dto.request.UserRequest
import com.example.ajaxproject.model.User
import com.mongodb.client.result.DeleteResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserService {

    fun create(userRequest: UserRequest): Mono<User>

    fun updateUser(userResponse: UserResponse): Mono<User>

    fun deleteUser(id: String): Mono<DeleteResult>

    fun getAll(page: Int, size: Int): Flux<User>

    fun getById(id: String): Mono<User>
}

package com.example.ajax.application.port

import com.example.ajax.domain.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserServiceInPort {

    fun updateUser(user: User): Mono<User>

    fun deleteUser(id: String): Mono<User>

    fun getAll(page: Int, size: Int): Flux<User>

    fun getById(id: String): Mono<User>

    fun save(user: User): Mono<User>

    fun create(user: User): Mono<User>
}

package com.example.ajax.application.repository

import com.example.ajax.domain.User
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepositoryOutPort {

    fun findAll(page: Pageable): Flux<User>

    fun save(user: User): Mono<User>

    fun findById(id: String): Mono<User>

    fun findByEmail(email: String): Mono<User>

    fun deleteById(userId: String): Mono<User>
}

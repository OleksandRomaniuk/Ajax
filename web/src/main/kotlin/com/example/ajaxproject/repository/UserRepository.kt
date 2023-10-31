package com.example.ajaxproject.repository

import com.example.ajaxproject.model.User
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepository{

    fun findAll(page: Pageable): Flux<User>

    fun save(user: User): Mono<User>

    fun findById(id: String): Mono<User>

    fun findByEmail(email: String): Mono<User>

    fun deleteById(userId: String): Mono<User>
}

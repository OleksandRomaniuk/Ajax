package com.example.ajaxproject.repository.impl

import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.UserRepository
import com.mongodb.client.result.DeleteResult
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UserRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
) : UserRepository {

    override fun findAll(page: Pageable): Flux<User> {
        return reactiveMongoTemplate.findAll(User::class.java)
    }

    override fun save(user: User): Mono<User> {
        return reactiveMongoTemplate.save(user)
    }

    override fun findById(id: String): Mono<User> {
        return reactiveMongoTemplate.findById(id, User::class.java)
    }

    override fun findByEmail(email: String): Mono<User> {
        val userQuery = Query.query(Criteria.where("email").`is`(email))
        return reactiveMongoTemplate.findOne(userQuery, User::class.java)
    }

    override fun deleteById(userId: String): Mono<DeleteResult> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(userId))
        return reactiveMongoTemplate.remove(query, User::class.java)
    }
}


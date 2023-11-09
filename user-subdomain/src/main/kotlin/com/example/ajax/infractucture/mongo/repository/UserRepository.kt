package com.example.ajax.infractucture.mongo.repository

import com.example.ajax.application.repository.UserRepositoryOutPort
import com.example.ajax.domain.User
import com.example.ajax.infractucture.mongo.entity.UserEntity
import com.example.ajax.infractucture.mongo.mapper.UserMapper
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class UserRepositoryImpl(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
    private val userMapper: UserMapper
) : UserRepositoryOutPort {

    override fun findAll(page: Pageable): Flux<User> {
        return reactiveMongoTemplate.findAll(UserEntity::class.java)
            .map { userMapper.mapToDomain(it) }
    }

    override fun save(user: User): Mono<User> {
        return reactiveMongoTemplate.save(userMapper.mapToEntity(user))
            .map { userMapper.mapToDomain(it) }
    }

    override fun findById(id: String): Mono<User> {
        return reactiveMongoTemplate.findById(id, UserEntity::class.java)
            .map { userMapper.mapToDomain(it) }
    }

    override fun findByEmail(email: String): Mono<User> {
        val userQuery = Query.query(Criteria.where("email").`is`(email))
        return reactiveMongoTemplate.findOne(userQuery, UserEntity::class.java)
            .map { userMapper.mapToDomain(it) }
    }

    override fun deleteById(userId: String): Mono<User> {
        val query = Query().addCriteria(Criteria.where("_id").`is`(userId))
        return reactiveMongoTemplate.findAndRemove(query, UserEntity::class.java)
            .map { userMapper.mapToDomain(it) }
    }
}

package com.example.ajax.application.service

import com.example.ajax.application.repository.UserRepositoryOutPort
import com.example.ajax.domain.User
import com.example.ajax.infractucture.mongo.mapper.UserMapper
import org.bson.types.ObjectId

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserRepositoryOutPort,
    private val userKafkaProducer: UserKafkaProducer,
    private val userMapper: UserMapper
) : UserService {

    override fun create(user: User): Mono<User> {
        return userRepository.save(
            User(
                ObjectId().toHexString(),
                user.email,
                user.password
            )
        )
    }

    override fun save(user: User): Mono<User> = userRepository.save(user)

    override fun updateUser(user: User): Mono<User> {
        return userRepository.findById(user.id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
            .flatMap { existingUser ->
                val updatedUser = existingUser.copy(
                    email = user.email,
                    password = user.password
                )
                userRepository.save(updatedUser)
            }
    }

    override fun deleteUser(id: String): Mono<User> {
        return userRepository.deleteById(id)
            .doOnNext {
                userKafkaProducer.sendUserDeleteEventToKafka(userMapper.(it))
            }
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
    }

    override fun getAll(page: Int, size: Int): Flux<User> =
        userRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
    }
}
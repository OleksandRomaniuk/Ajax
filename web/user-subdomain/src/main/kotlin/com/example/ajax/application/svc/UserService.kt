package com.example.ajax.application.svc

import com.example.ajax.application.port.UserKafkaProducerOutPort
import com.example.ajax.application.port.UserRepositoryOutPort
import com.example.ajax.application.port.UserServiceInPort
import com.example.ajax.domain.User
import com.example.ajax.domain.exception.NotFoundException
import com.example.ajax.extention.doMonoOnNext
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepositoryOutPort,
    private val userKafkaProducer: UserKafkaProducerOutPort,
) : UserServiceInPort {

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
            .doMonoOnNext {
                userKafkaProducer.sendUserUpdatedEventToKafka(it)
            }
    }

    override fun deleteUser(id: String): Mono<User> {
        return userRepository.deleteById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
    }

    override fun getAll(page: Int, size: Int): Flux<User> =
        userRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
    }
}

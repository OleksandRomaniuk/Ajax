package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.UserDTO
import com.example.ajaxproject.dto.request.UserRequest
import com.example.ajaxproject.exeption.NotFoundException
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.GroupChatRoomRepository
import com.example.ajaxproject.repository.UserRepository
import com.example.ajaxproject.service.interfaces.UserService
import com.mongodb.client.result.DeleteResult
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val groupChatRoomRepository: GroupChatRoomRepository
) : UserService {

    override fun create(userRequest: UserRequest): Mono<User> {
        return userRepository.save(
            User(
                ObjectId().toHexString(),
                userRequest.email,
                userRequest.password
            )
        )
    }

    override fun save(userDTO: UserDTO): Mono<User> = userRepository.save(toEntity(userDTO))

    override fun updateUser(userResponse: UserDTO): Mono<User> {
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
        return groupChatRoomRepository.removeUserFromAllChats(id)
            .then(userRepository.deleteById(id))
            .handle { deletedResult, sink ->
            if (deletedResult.deletedCount > 0) {
                sink.next(deletedResult)
            } else {
                sink.error(NotFoundException("User with id $id not found"))
            }
        }
    }

    override fun getAll(page: Int, size: Int): Flux<User> =
        userRepository.findAll(PageRequest.of(page, size))

    override fun getById(id: String): Mono<User> {
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("User not found")))
    }

    fun toEntity(userDTO: UserDTO): User {
        return User(
            id = userDTO.id,
            email = userDTO.email,
            password = userDTO.password,
        )
    }
}

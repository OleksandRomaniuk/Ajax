package com.example.ajaxproject.nats

import com.example.ajax.User
import com.example.ajaxproject.NatsSubject.UserRequest

import com.example.ajaxproject.model.toUserDTO
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import com.reqreply.user.CreateUserRequest
import com.reqreply.user.CreateUserResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class CreateUserNatsController(
    private val service: UserService,
    override val connection: Connection,
    private val userMapper: UserMapper
) : NatsController<CreateUserRequest, CreateUserResponse> {

    override val subject: String = UserRequest.ADD_USER

    override val parser: Parser<CreateUserRequest> =
        CreateUserRequest.parser()

    override fun handle(request: CreateUserRequest): Mono<CreateUserResponse> {
        val user = userMapper.protoToUser(request.user).toUserDTO()
        return service.save(user)
            .map { buildSuccessResponse(userMapper.userToProto(it)) }
            .onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    }

    private fun buildSuccessResponse(user: User): CreateUserResponse =
        CreateUserResponse.newBuilder().apply {
            successBuilder.setUser(user)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): CreateUserResponse =
        CreateUserResponse.newBuilder().apply {
            failureBuilder.setMessage("User creation failed by $exception: $message")
        }.build()
}

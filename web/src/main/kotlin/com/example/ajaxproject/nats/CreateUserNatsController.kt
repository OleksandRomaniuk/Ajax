package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.ADD_USER
import com.example.ajaxproject.UserOuterClass.CreateUserRequest
import com.example.ajaxproject.UserOuterClass.CreateUserResponse
import com.example.ajaxproject.model.toUserDTO
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class CreateUserNatsController(
    private val service: UserService,
    override val connection: Connection,
    private val userMapper: UserMapper
) : NatsController<CreateUserRequest, CreateUserResponse> {

    override val subject: String = ADD_USER

    override val parser: Parser<CreateUserRequest> =
        CreateUserRequest.parser()

    override fun generateReplyForNatsRequest(request: CreateUserRequest): Mono<CreateUserResponse> {
        return service.save(userMapper.protoRequestToUser(request).toUserDTO())
            .map { userMapper.userToProtoResponse(it) }
    }
}


package com.example.ajax.infrastructure.nats

import com.example.ajax.NatsSubject.UserRequest
import com.example.ajax.User
import com.example.ajax.application.port.UserServiceInPort
import com.example.ajax.infrastructure.mapper.UserMapper
import com.example.ajax.infrastructure.adapters.nats.NatsController
import com.google.protobuf.Parser
import com.reqreply.user.CreateUserRequest
import com.reqreply.user.CreateUserResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class CreateUserNatsController(
    private val service: UserServiceInPort,
    override val connection: Connection,
    private val userMapper: UserMapper
) : NatsController<CreateUserRequest, CreateUserResponse> {

    override val subject: String = UserRequest.ADD_USER

    override val parser: Parser<CreateUserRequest> =
        CreateUserRequest.parser()

    override fun handle(request: CreateUserRequest): Mono<CreateUserResponse> {
        val user = userMapper.protoToUser(request.user)
        return service.save(user)
            .map { buildSuccessResponse(userMapper.mapToProto(it)) }
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

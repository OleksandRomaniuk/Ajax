package com.example.ajaxproject.nats

import com.example.ajaxproject.CreateUserRequest
import com.example.ajaxproject.CreateUserResponse
import com.example.ajaxproject.NatsSubject.ADD_USER
import com.example.ajaxproject.User
import com.example.ajaxproject.dto.request.toProtoUser
import com.example.ajaxproject.dto.request.toUserRequest
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class CreateUserNatsController(
    override val connection: Connection,
    private val userService: UserService,
) : NatsController<CreateUserRequest, CreateUserResponse> {

    override val subject = ADD_USER
    override val parser: Parser<CreateUserRequest> = CreateUserRequest.parser()

    override fun handle(request: CreateUserRequest): CreateUserResponse = runCatching {
        val saveUser = userService.create(request.user.toUserRequest())
        buildSuccessResponse(saveUser.toProtoUser())
    }.getOrElse { exception ->
        buildFailureResponse(exception.javaClass.simpleName, exception.toString())
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

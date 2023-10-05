package com.example.ajaxproject.nats

import com.example.ajaxproject.GetByIdUserRequest
import com.example.ajaxproject.GetByIdUserResponse
import com.example.ajaxproject.NatsSubject.GET_USER_BY_ID
import com.example.ajaxproject.User
import com.example.ajaxproject.model.toProtoUser
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class GetUserByIdNatsController(
    override val connection: Connection,
    private val userService: UserService,
) : NatsController<GetByIdUserRequest, GetByIdUserResponse> {

    override val subject = GET_USER_BY_ID
    override val parser: Parser<GetByIdUserRequest> = GetByIdUserRequest.parser()

    override fun handle(request: GetByIdUserRequest): GetByIdUserResponse = runCatching {
        val getUserById = userService.getUserById(request.userId)
        buildSuccessResponse(getUserById.toProtoUser())
    }.getOrElse { exception ->
        buildFailureResponse(exception.javaClass.simpleName, exception.toString())
    }

    private fun buildSuccessResponse(user: User): GetByIdUserResponse =
        GetByIdUserResponse.newBuilder().apply {
            successBuilder.setUser(user)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): GetByIdUserResponse =
        GetByIdUserResponse.newBuilder().apply {
            failureBuilder.setMessage("User find by id failed by $exception: $message")
        }.build()
}

package com.example.ajaxproject.nats

import com.example.ajaxproject.GetAllUsersRequest
import com.example.ajaxproject.GetAllUsersResponse
import com.example.ajaxproject.NatsSubject.GET_ALL_USERS
import com.example.ajaxproject.User
import com.example.ajaxproject.model.toProtoUser
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection

import org.springframework.stereotype.Component

@Component
class GetAllUsersNatsController(
    override val connection: Connection,
    private val userService: UserService,
) : NatsController<GetAllUsersRequest, GetAllUsersResponse> {

    override val subject = GET_ALL_USERS
    override val parser: Parser<GetAllUsersRequest> = GetAllUsersRequest.parser()

    override fun handle(request: GetAllUsersRequest): GetAllUsersResponse = runCatching {
        buildSuccessResponse(userService.getAllUsers().map { it.toProtoUser() })
    }.getOrElse { exception ->
        buildFailureResponse(exception.javaClass.simpleName, exception.toString())
    }

    private fun buildSuccessResponse(usersList: List<User>): GetAllUsersResponse =
        GetAllUsersResponse.newBuilder().apply {
            successBuilder.usersBuilder.addAllUser(usersList)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): GetAllUsersResponse =
        GetAllUsersResponse.newBuilder().apply {
            failureBuilder
                .setMessage("users find failed by $exception: $message")
        }.build()
}

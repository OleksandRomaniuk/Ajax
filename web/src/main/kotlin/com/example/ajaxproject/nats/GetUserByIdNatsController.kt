package com.example.ajaxproject.nats

import com.example.ajax.User
import com.example.ajaxproject.NatsSubject.UserRequest
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import com.reqreply.user.GetByIdUserRequest
import com.reqreply.user.GetByIdUserResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class GetUserByIdNatsController(
    private val userMapper: UserMapper,
    private val userService: UserService,
    override val connection: Connection,
) : NatsController<GetByIdUserRequest, GetByIdUserResponse> {

    override val subject: String = UserRequest.GET_USER_BY_ID

    override val parser: Parser<GetByIdUserRequest> = GetByIdUserRequest.parser()

    override fun handle(request: GetByIdUserRequest): Mono<GetByIdUserResponse> {
        return userService.getById(request.userId)
            .map { buildSuccessResponse(userMapper.userToProto(it)) }
            .onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
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


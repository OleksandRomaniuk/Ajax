package com.example.ajaxproject.nats

import com.example.ajax.User
import com.example.ajaxproject.NatsSubject.UserRequest
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import com.reqreply.user.GetAllUsersRequest
import com.reqreply.user.GetAllUsersResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class GetAllUsersNatsController(
    private val userMapper: UserMapper,
    private val userService: UserService,
    override val connection: Connection,
) : NatsController<GetAllUsersRequest, GetAllUsersResponse> {

    override val subject: String = UserRequest.GET_ALL_USERS

    override val parser: Parser<GetAllUsersRequest> = GetAllUsersRequest.parser()

    override fun handle(request: GetAllUsersRequest): Mono<GetAllUsersResponse> {
        return userService.getAll(DEFAULT_LIMIT, DEFAULT_OFFSET)
            .collectList()
            .map { users -> buildSuccessResponse(users.map { userMapper.userToProto(it) }) }
            .onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    }

    private fun buildSuccessResponse(deviceList: List<User>): GetAllUsersResponse =
        GetAllUsersResponse.newBuilder().apply {
            successBuilder.addAllUser(deviceList)
        }.build()

    private fun buildFailureResponse(exception: String, message: String): GetAllUsersResponse =
        GetAllUsersResponse.newBuilder().apply {
            failureBuilder.setMessage("User find failed by $exception: $message")
        }.build()

    private companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 25
    }
}

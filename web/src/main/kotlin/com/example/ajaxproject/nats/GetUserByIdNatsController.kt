package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.GET_USER_BY_ID
import com.example.ajaxproject.UserOuterClass
import com.example.ajaxproject.UserOuterClass.GetByIdUserRequest
import com.example.ajaxproject.UserOuterClass.GetByIdUserResponse
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetUserByIdNatsController(
    private val userMapper: UserMapper,
    private val userService: UserService,
    override val connection: Connection,
) : NatsController<GetByIdUserRequest, GetByIdUserResponse> {

    override val subject: String = GET_USER_BY_ID

    override val parser: Parser<GetByIdUserRequest> = GetByIdUserRequest.parser()

    override fun generateReplyForNatsRequest(request: GetByIdUserRequest): Mono<GetByIdUserResponse> {
        val userId = request.userId

        return userService.getById(userId)
            .map { userMapper.userToProto(it) }
            .map {
                GetByIdUserResponse.newBuilder()
                    .setUser(it)
                    .build()
            }
    }
}


package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.UserRequest
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

    override val subject: String = UserRequest.GET_USER_BY_ID

    override val parser: Parser<GetByIdUserRequest> = GetByIdUserRequest.parser()

    override fun generateReplyForNatsRequest(request: GetByIdUserRequest): Mono<GetByIdUserResponse> {

        return userService.getById(request.userId)
            .map {
                val protoUser = userMapper.userToProto(it)
                GetByIdUserResponse.newBuilder().setUser(protoUser).build()
            }
    }
}


package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.GET_ALL_USERS
import com.example.ajaxproject.UserOuterClass.GetAllUsersResponse
import com.example.ajaxproject.UserOuterClass.UserList
import com.example.ajaxproject.UserOuterClass.GetAllUsersRequest
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetUserAllNatsController(
    private val userMapper: UserMapper,
    private val userService: UserService,
    override val connection: Connection
) : NatsController<GetAllUsersRequest, GetAllUsersResponse> {

    override val subject: String = GET_ALL_USERS

    private companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 25
    }

    override val parser: Parser<GetAllUsersRequest> = GetAllUsersRequest.parser()

    override fun generateReplyForNatsRequest(request: GetAllUsersRequest): Mono<GetAllUsersResponse> {
        return userService.getAll(DEFAULT_OFFSET , DEFAULT_LIMIT)
            .collectList()
            .map { userList ->
                val userProtoList = userList.map { userMapper.userToProto(it) }
                GetAllUsersResponse.newBuilder()
                    .setUsers(UserList.newBuilder().addAllUser(userProtoList).build())
                    .build()
            }
    }
}



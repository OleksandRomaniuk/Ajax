package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.GET_ALL_USERS
import com.example.ajaxproject.UserOuterClass.GetAllUsersRequest
import com.example.ajaxproject.UserOuterClass.GetAllUsersResponse
import com.example.ajaxproject.UserOuterClass.UserList
import com.example.ajaxproject.repository.UserRepository
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GetAllUsersNatsController(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    override val connection: Connection,
) : NatsController<GetAllUsersRequest, GetAllUsersResponse> {

    override val subject: String = GET_ALL_USERS

    override val parser: Parser<GetAllUsersRequest> = GetAllUsersRequest.parser()

    override fun generateReplyForNatsRequest(request: GetAllUsersRequest): Mono<GetAllUsersResponse> {
        return userRepository.findAll(PageRequest.of(DEFAULT_OFFSET, DEFAULT_LIMIT))
            .map { userMapper.userToProto(it) }
            .reduce(UserList.newBuilder(), UserList.Builder::addUser)
            .map { usersListBuilder ->
                GetAllUsersResponse.newBuilder().setUsers(usersListBuilder).build()
            }
    }

    private companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 25
    }
}

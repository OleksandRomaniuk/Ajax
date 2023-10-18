package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.DELETE_USER_BY_ID
import com.example.ajaxproject.UserOuterClass.DeleteUserRequest
import com.example.ajaxproject.UserOuterClass.DeleteUserResponse
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class DeleteUserNatsController(
    private val userService: UserService,
    override val connection: Connection
) : NatsController<DeleteUserRequest, DeleteUserResponse> {

    override val subject: String = DELETE_USER_BY_ID

    override val parser: Parser<DeleteUserRequest> = DeleteUserRequest.parser()

    override fun generateReplyForNatsRequest(request: DeleteUserRequest): Mono<DeleteUserResponse> {
        return userService.deleteUser(request.userId)
            .thenReturn(DeleteUserResponse.getDefaultInstance())
    }
}

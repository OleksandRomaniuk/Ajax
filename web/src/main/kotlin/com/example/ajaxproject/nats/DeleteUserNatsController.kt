package com.example.ajaxproject.nats

import com.example.ajaxproject.NatsSubject.UserRequest
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import com.reqreply.user.DeleteUserRequest
import com.reqreply.user.DeleteUserResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class DeleteUserNatsController(
    private val service: UserService,
    override val connection: Connection
) : NatsController<DeleteUserRequest, DeleteUserResponse> {

    override val subject: String = UserRequest.DELETE_USER_BY_ID

    override val parser: Parser<DeleteUserRequest> = DeleteUserRequest.parser()

    override fun handle(request: DeleteUserRequest): Mono<DeleteUserResponse> {
        return service.deleteUser(request.userId).map { buildSuccessResponse() }.onErrorResume { exception ->
                buildFailureResponse(
                    exception.javaClass.simpleName, exception.toString()
                ).toMono()
            }
    }

    private fun buildSuccessResponse(): DeleteUserResponse = DeleteUserResponse.newBuilder().apply {
        successBuilder.build()
    }.build()

    private fun buildFailureResponse(exception: String, message: String): DeleteUserResponse =
        DeleteUserResponse.newBuilder().apply {
            failureBuilder.setMessage("User delete failed by $exception: $message")
        }.build()
}

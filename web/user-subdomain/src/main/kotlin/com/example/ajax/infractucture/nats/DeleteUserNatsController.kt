package com.example.ajax.infractucture.nats

import com.example.ajax.NatsSubject.UserRequest
import com.example.ajax.application.service.UserServiceOutPort
import com.example.ajax.infrastructure.adapters.nats.NatsController
import com.google.protobuf.Parser
import com.reqreply.user.DeleteUserRequest
import com.reqreply.user.DeleteUserResponse
import io.nats.client.Connection
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class DeleteUserNatsController(
    private val service: UserServiceOutPort,
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

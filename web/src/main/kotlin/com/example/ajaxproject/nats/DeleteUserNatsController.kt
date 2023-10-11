/*
package com.example.ajaxproject.nats

import com.example.ajaxproject.DeleteUserRequest
import com.example.ajaxproject.DeleteUserResponse
import com.example.ajaxproject.NatsSubject.DELETE_USER_BY_ID
import com.example.ajaxproject.service.interfaces.UserService
import com.google.protobuf.Parser
import io.nats.client.Connection
import org.springframework.stereotype.Component

@Component
class DeleteUserNatsController(
    override val connection: Connection,
    private val userService: UserService,
) : NatsController<DeleteUserRequest, DeleteUserResponse> {

    override val subject = DELETE_USER_BY_ID
    override val parser: Parser<DeleteUserRequest> = DeleteUserRequest.parser()

    override fun handle(request: DeleteUserRequest): DeleteUserResponse = runCatching {
        userService.deleteUser(request.userId)
        buildSuccessResponse()
    }.getOrElse { exception ->
        buildFailureResponse(exception.javaClass.simpleName, exception.toString())
    }

    private fun buildSuccessResponse(): DeleteUserResponse =
        DeleteUserResponse.newBuilder().apply {
            successBuilder.build()
        }.build()

    private fun buildFailureResponse(exception: String, message: String): DeleteUserResponse =
        DeleteUserResponse.newBuilder().apply {
            failureBuilder.setMessage("User delete failed by $exception: $message")
        }.build()
}
*/

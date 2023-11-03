package com.example.ajaxproject.controller.grps

import com.example.ajax.User
import com.example.ajax.UserList
import com.example.ajaxproject.KafkaTopic
import com.example.ajaxproject.nats.UserEventNatsService
import com.example.ajaxproject.nats.UserMapper
import com.example.ajaxproject.service.interfaces.UserService
import com.pubsub.user.UserUpdatedEvent
import com.reqreply.user.DeleteUserRequest
import com.reqreply.user.DeleteUserResponse
import com.reqreply.user.GetAllUsersRequest
import com.reqreply.user.GetAllUsersResponse
import com.reqreply.user.GetByIdUserRequest
import com.reqreply.user.GetByIdUserResponse
import com.reqreply.user.StreamRequest
import com.reqreply.user.StreamResponse
import com.service.user.ReactorUserServiceGrpc
import net.devh.boot.grpc.server.service.GrpcService
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@GrpcService
class GrpcUserService(
    private val userService: UserService,
    private val userMapper: UserMapper,
    private val userEventNatsService: UserEventNatsService<UserUpdatedEvent>
) : ReactorUserServiceGrpc.UserServiceImplBase() {

    override fun getById(request: Mono<GetByIdUserRequest>): Mono<GetByIdUserResponse> {
        return request.flatMap { handleGetById(it) }
            .onErrorResume { exception ->
                buildFailureResponseGetById(
                    exception.javaClass.simpleName,
                    exception.toString()
                ).toMono()
            }
    }

    override fun delete(request: Mono<DeleteUserRequest>): Mono<DeleteUserResponse> {
        return request.flatMap {
            userService.deleteUser(it.userId).map { buildSuccessDeleteResponse() }
                .onErrorResume { exception ->
                    DeleteUserResponse.newBuilder().apply {
                        failureBuilder.setMessage("Deleting user failed by " +
                                "${exception.javaClass.simpleName}: ${exception.message}")
                    }.build().toMono()
                }
        }
    }

    override fun getAll(request: Mono<GetAllUsersRequest>): Mono<GetAllUsersResponse> {
        return userService.getAll(DEFAULT_LIMIT, DEFAULT_OFFSET).map { userMapper.userToProto(it) }
            .reduce(UserList.newBuilder(), UserList.Builder::addUser).map { usersListBuilder ->
                GetAllUsersResponse.newBuilder().apply {
                    successBuilder.addAllUser(usersListBuilder.build().userList)
                }.build()
            }.onErrorResume { exception ->
                buildFailureResponseGetAll(
                    exception.javaClass.simpleName, exception.toString()
                ).toMono()
            }
    }

    override fun stream(request: Mono<StreamRequest>): Flux<StreamResponse> =
        request.flatMapMany {
            userService.getById(it.userId)
                .flatMapMany { user ->
                    userEventNatsService.subscribeToEvents(it.userId, KafkaTopic.User.UPDATE)
                        .map { buildSuccessResponseStreamById(it.user) }
                        .startWith(buildSuccessResponseStreamById(userMapper.userToProto(user)))
                }
                .onErrorResume { exception ->
                    StreamResponse.newBuilder().apply {
                        failureBuilder.setMessage(
                            "User stream failed by " +
                                    "${exception.javaClass.simpleName}: ${exception.message}"
                        )
                    }.build()
                        .toMono()
                }
        }

    private fun buildSuccessResponseStreamById(user: User): StreamResponse =
        StreamResponse.newBuilder().apply {
            successBuilder.setUser(user)
        }.build()

    private fun buildSuccessDeleteResponse(): DeleteUserResponse = DeleteUserResponse
        .newBuilder().apply {
            successBuilder.build()
        }.build()

    private fun handleGetById(request: GetByIdUserRequest): Mono<GetByIdUserResponse> =
        userService.getById(request.userId)
            .map { buildSuccessResponseGetById(userMapper.userToProto(it)) }

    private fun buildFailureResponseGetAll(exception: String, message: String): GetAllUsersResponse =
        GetAllUsersResponse.newBuilder().apply {
            failureBuilder.setMessage("User find all failed by $exception: $message")
        }.build()

    private fun buildSuccessResponseGetById(user: User): GetByIdUserResponse =
        GetByIdUserResponse.newBuilder().apply {
            successBuilder.setUser(user)
        }.build()

    private fun buildFailureResponseGetById(exception: String, message: String): GetByIdUserResponse =
        GetByIdUserResponse.newBuilder().apply {
            failureBuilder.setMessage("User find by id failed by $exception: $message")
        }.build()

    private companion object {
        const val DEFAULT_OFFSET = 0
        const val DEFAULT_LIMIT = 25
    }
}

package com.example.ajax.infrastructure.rest.grps

import com.example.ajax.User
import com.example.ajax.UserList
import com.example.ajax.application.port.UserServiceInPort
import com.example.ajax.infrastructure.mapper.UserMapper
import com.example.ajax.infrastructure.adapters.nats.EventNatsService
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
    private val userServiceInPort: UserServiceInPort,
    private val userMapper: UserMapper,
    private val userEventNatsService: EventNatsService<UserUpdatedEvent>
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
            userServiceInPort.deleteUser(it.userId).map { buildSuccessDeleteResponse() }
                .onErrorResume { exception ->
                    DeleteUserResponse.newBuilder().apply {
                        failureBuilder.setMessage(
                            "Deleting user failed by " +
                                    "${exception.javaClass.simpleName}: ${exception.message}"
                        )
                    }.build().toMono()
                }
        }
    }

    override fun getAll(request: Mono<GetAllUsersRequest>): Mono<GetAllUsersResponse> {
        return userServiceInPort.getAll(DEFAULT_LIMIT, DEFAULT_OFFSET).map { userMapper.mapToProto(it) }
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
            userServiceInPort.getById(it.userId)
                .flatMapMany { user ->
                    userEventNatsService.subscribeToEvents(it.userId)
                        .map { buildSuccessResponseStream(it.user) }
                        .startWith(buildSuccessResponseStream(userMapper.mapToProto(user)))
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

    private fun buildSuccessResponseStream(user: User): StreamResponse =
        StreamResponse.newBuilder().apply {
            successBuilder.setUser(user)
        }.build()

    private fun buildSuccessDeleteResponse(): DeleteUserResponse = DeleteUserResponse
        .newBuilder().apply {
            successBuilder.build()
        }.build()

    private fun handleGetById(request: GetByIdUserRequest): Mono<GetByIdUserResponse> =
        userServiceInPort.getById(request.userId)
            .map { buildSuccessResponseGetById(userMapper.mapToProto(it)) }

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

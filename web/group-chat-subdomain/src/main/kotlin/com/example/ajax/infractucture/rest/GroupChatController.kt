package com.example.ajax.infractucture.rest

import com.example.ajax.application.service.GroupChatServiceOutPort
import com.example.ajax.domain.GroupChatRoom
import com.example.ajax.domain.User
import com.example.ajax.infractucture.rest.dto.CreateChatRequest
import com.example.ajax.infractucture.rest.dto.LeaveChatRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chat")
class GroupChatController(
    val groupChatServiceOutPort: GroupChatServiceOutPort
) {
    @PostMapping("/create")
    fun createRoom(@RequestBody createChatRequest: CreateChatRequest): Mono<GroupChatRoom> {
        return groupChatServiceOutPort.createGroupRoom(createChatRequest)
    }

    @GetMapping("/{chatId}/users")
    fun getAllChatUsers(@PathVariable chatId: String): Flux<User> {
        return groupChatServiceOutPort.getAllChatMembers(chatId)
    }

    @PostMapping("/{chatId}/addUser/{userId}")
    fun addUserToChat(@PathVariable chatId: String, @PathVariable userId: String): Mono<GroupChatRoom> {
        return groupChatServiceOutPort.addUserToChat(chatId, userId)
    }

    @PostMapping("/leaveChat")
    fun leaveChat(@RequestBody leaveChatRequest: LeaveChatRequest): Mono<String> {
        return groupChatServiceOutPort.leaveGroupChat(leaveChatRequest.userId, leaveChatRequest.chatId)
    }
}

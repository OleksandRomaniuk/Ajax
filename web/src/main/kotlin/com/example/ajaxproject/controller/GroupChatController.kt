package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.CreateChatDTO
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.request.LeaveChatDTO
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.service.interfaces.GroupChatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chat")
class GroupChatController(
    val groupChatService: GroupChatService
) {
    @PostMapping("/create")
    fun createRoom(@RequestBody createChatDto: CreateChatDTO): Mono<GroupChatRoom> {
        return groupChatService.createGroupRoom(createChatDto)
    }

    @GetMapping("/{chatId}/users")
    fun getAllChatUsers(@PathVariable chatId: String): Mono<List<User>> {
        return groupChatService.getAllChatMembers(chatId)
    }

    @PostMapping("/{chatId}/addUser/{userId}")
    fun addUserToChat(@PathVariable chatId: String, @PathVariable userId: String): Mono<User> {
        return groupChatService.addUserToChat(chatId, userId)
    }

    @PostMapping("/sendGroupMessage")
    fun sendMessage(@RequestBody groupChatDTO: GroupChatDto): Mono<GroupChatMessageResponse> {
        return groupChatService.sendMessageToGroup(groupChatDTO)
    }

    @GetMapping("/getAllMessages/{chatId}")
    fun getAllMessages(@PathVariable chatId: String): Mono<List<GroupChatMessageResponse>> {
        return groupChatService.getAllGroupMessages(chatId)
    }

    @PostMapping("/leaveChat")
    fun leaveChat(@RequestBody leaveChatDTO: LeaveChatDTO): Mono<String> {
        return groupChatService.leaveGroupChat(leaveChatDTO.userId, leaveChatDTO.chatId)
    }
}

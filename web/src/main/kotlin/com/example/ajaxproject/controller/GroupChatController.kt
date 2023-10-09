package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.dto.responce.OffsetPaginateResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.service.interfaces.GroupChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chat")
class GroupChatController @Autowired constructor(
    private val groupChatService: GroupChatService
) {

    @PostMapping("/create")
    fun createRoom(@RequestBody createChatDto: CreateChatDto): Mono<ResponseEntity<GroupChatRoom>> {
        return groupChatService.createGroupRoom(createChatDto)
            .map { ResponseEntity.ok(it) }
    }

    @GetMapping("/{chatId}/users")
    fun getAllChatUsers(@PathVariable chatId: String): Mono<ResponseEntity<List<User>>> {
        return groupChatService.getAllChatMembers(chatId)
            .collectList()
            .map { ResponseEntity.ok(it) }
    }

    @PostMapping("/{chatId}/addUser/{userId}")
    fun addUserToChat(@PathVariable chatId: String, @PathVariable userId: String): Mono<ResponseEntity<List<User>>> {
        return groupChatService.addUserToChat(chatId, userId)
            .map { ResponseEntity.ok(it) }
    }

    @PostMapping("/sendGroupMessage")
    fun sendMessage(@RequestBody groupChatDTO: GroupChatDto): Mono<ResponseEntity<GroupChatMessageResponse>> {
        return groupChatService.sendMessageToGroup(groupChatDTO)
            .map { ResponseEntity.ok(it) }
    }

    @GetMapping("/getAllMessages/{chatId}")
    fun getAllMessages(@PathVariable chatId: String): Mono<ResponseEntity<List<GroupChatMessageResponse>>> {
        return groupChatService.getAllGroupMessages(chatId)
            .collectList()
            .map { ResponseEntity.ok(it) }
    }

    @PostMapping("/leaveChat")
    fun leaveChat(@RequestBody chatDto: GroupChatDto): Mono<ResponseEntity<Boolean>> {
        return groupChatService.leaveGroupChat(chatDto.chatId, chatDto.senderId)
            .map { ResponseEntity.ok(it) }
    }

    @GetMapping("/messages")
    fun getMessagesByChatRoomId(
        @RequestParam chatRoomId: String,
        @RequestParam(defaultValue = "0") offset: Int,
        @RequestParam(defaultValue = "50") limit: Int
    ): Mono<OffsetPaginateResponse> {
        return groupChatService.getMessagesByChatRoomIdWithPagination(chatRoomId, limit, offset)
    }
}

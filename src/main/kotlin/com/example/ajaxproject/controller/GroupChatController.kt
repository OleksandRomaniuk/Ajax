package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GroupChatDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
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
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class GroupChatController @Autowired constructor(
    private val groupChatService: GroupChatService
) {

    @PostMapping("/create")
    fun createRoom(@RequestBody createChatDto: CreateChatDto): ResponseEntity<GroupChatRoom> {
        return ResponseEntity.ok(groupChatService.createGroupRoom(createChatDto))
    }

    @GetMapping("/{chatId}/users")
    fun getAllChatUsers(@PathVariable chatId: String): ResponseEntity<List<User>> {
        return ResponseEntity.ok(groupChatService.getAllChatMembers(chatId))
    }

    @PostMapping("/{chatId}/addUser/{userId}")
    fun addUserToChat(@PathVariable chatId: String, @PathVariable userId: String): ResponseEntity<List<User>> {
        return ResponseEntity.ok(groupChatService.addUserToChat(chatId, userId))
    }

    @PostMapping("/sendGroupMessage")
    fun sendMessage(@RequestBody groupChatDTO: GroupChatDto): ResponseEntity<GroupChatMessageResponse> {
        return ResponseEntity.ok(groupChatService.sendMessageToGroup(groupChatDTO))
    }

    @GetMapping("/getAllMessages/{chatId}")
    fun getAllMessages(@PathVariable chatId: String): ResponseEntity<List<GroupChatMessageResponse>> {
        return ResponseEntity.ok(groupChatService.getAllGroupMessages(chatId))
    }

    @PostMapping("/leaveChat")
    fun leaveChat(@RequestBody chatDto: GroupChatDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(groupChatService.leaveGroupChat(chatDto.chatId, chatDto.senderId))
    }
}

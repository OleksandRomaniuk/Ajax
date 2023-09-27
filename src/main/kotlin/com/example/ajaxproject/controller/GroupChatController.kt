package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.ChatDTO
import com.example.ajaxproject.dto.request.CreateChatDto
import com.example.ajaxproject.dto.request.GetAllMessagesByDayRequestDto
import com.example.ajaxproject.dto.request.GroupChatDTO
import com.example.ajaxproject.dto.responce.DayMessagesDto
import com.example.ajaxproject.dto.responce.GetAllMessagesByDayResponseDto
import com.example.ajaxproject.dto.responce.GroupChatMessageDto
import com.example.ajaxproject.dto.responce.GroupChatMessageResponse
import com.example.ajaxproject.model.GroupChatRoom
import com.example.ajaxproject.model.User
import com.example.ajaxproject.repository.impl.DayMessages
import com.example.ajaxproject.service.interfaces.GroupChatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
    fun sendMessage(@RequestBody groupChatDTO: GroupChatDTO): ResponseEntity<GroupChatMessageResponse> {
        return ResponseEntity.ok(groupChatService.sendMessageToGroup(groupChatDTO))
    }

    @GetMapping("/by-day")
    fun getAllMessagesByDay(@RequestBody requestDto: GetAllMessagesByDayRequestDto): ResponseEntity<Page<DayMessagesDto>> {
        val messages = groupChatService.findAllMessagesByDay(requestDto.chatRoomId, requestDto.pageable)

        val dayMessagesDtoList = messages.map { message ->
            val groupChatMessageDto = GroupChatMessageDto(message)
            val day
            DayMessagesDto(day, listOf(groupChatMessageDto))
        }

        val responseDto = GetAllMessagesByDayResponseDto(
            content = dayMessagesDtoList,
            totalPages = messages.totalPages,
            totalElements = messages.totalElements
        )

        return ResponseEntity.ok(responseDto)
    }

    @PostMapping("/leaveChat")
    fun leaveChat(@RequestBody chatDto: ChatDTO): ResponseEntity<Boolean> {
        return ResponseEntity.ok(groupChatService.leaveGroupChat(chatDto.chatId, chatDto.senderId))
    }
}

package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.RoomDTO
import com.example.ajaxproject.dto.SendMessageDTO
import com.example.ajaxproject.model.ChatMessage
import com.example.ajaxproject.model.ChatRoom
import com.example.ajaxproject.service.interfaces.ChatService
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
class ChatController @Autowired constructor(
    private val chatService: ChatService
) {

    @PostMapping("/createRoom")
    fun createRoom(@RequestBody roomDTO: RoomDTO): ResponseEntity<ChatRoom> {
        return ResponseEntity.ok(
            chatService.createRoom(roomDTO.senderId, roomDTO.recipientId))
    }

    @GetMapping("/findRoom/{roomId}")
    fun findRoom(@PathVariable roomId: String): ResponseEntity<ChatRoom> {
        return ResponseEntity.ok(
            chatService.getRoom(roomId))
    }

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody sendMessageDTO: SendMessageDTO
    ): ResponseEntity<ChatMessage> {
        return ResponseEntity.ok(
            chatService.sendMessage(sendMessageDTO))
    }

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody roomDTO: RoomDTO): ResponseEntity<List<ChatMessage>> {
        return ResponseEntity.ok(chatService.getAllMessages(roomDTO))
    }



}

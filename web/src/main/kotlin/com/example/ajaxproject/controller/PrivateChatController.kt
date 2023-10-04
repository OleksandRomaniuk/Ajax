package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.dto.request.RoomDTO
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.service.interfaces.PrivateChatService
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
class PrivateChatController @Autowired constructor(
    private val privateChatService: PrivateChatService
) {
    @PostMapping("/createRoom")
    fun createRoom(@RequestBody roomDTO: RoomDTO): ResponseEntity<PrivateChatRoom> {
        return ResponseEntity.ok(privateChatService.createPrivateRoom(roomDTO.senderId, roomDTO.recipientId))
    }

    @GetMapping("/findRoom/{roomId}")
    fun findRoom(@PathVariable roomId: String): ResponseEntity<PrivateChatRoom> {
        return ResponseEntity.ok(privateChatService.getPrivateRoom(roomId))
    }

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody privateMessageDTO: PrivateMessageDTO): ResponseEntity<PrivateChatMessage> {
        return ResponseEntity.ok(privateChatService.sendPrivateMessage(privateMessageDTO))
    }

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody roomDTO: RoomDTO): ResponseEntity<List<PrivateChatMessage>> {
        return ResponseEntity.ok(privateChatService.getAllPrivateMessages(roomDTO))
    }
}

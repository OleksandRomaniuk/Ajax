package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.dto.request.RoomDTO
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.service.interfaces.PrivateChatService
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
class PrivateChatController(
     val privateChatService: PrivateChatService
) {

    @PostMapping("/createRoom")
    fun createRoom(@RequestBody roomDTO: RoomDTO): Mono<PrivateChatRoom> =
        privateChatService.createPrivateRoom(roomDTO.senderId, roomDTO.recipientId)

    @GetMapping("/findRoom/{roomId}")
    fun findRoom(@PathVariable roomId: String): Mono<PrivateChatRoom?> =
        privateChatService.getPrivateRoom(roomId).map { it }

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody privateMessageDTO: PrivateMessageDTO): Mono<PrivateChatMessage> =
        privateChatService.sendPrivateMessage(privateMessageDTO)

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody roomDTO: RoomDTO): Mono<List<PrivateChatMessage>> =
        privateChatService.getAllPrivateMessages(roomDTO)
}

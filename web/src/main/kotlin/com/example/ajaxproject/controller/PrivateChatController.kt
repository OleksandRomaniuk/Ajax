package com.example.ajaxproject.controller

import com.example.ajaxproject.dto.request.PrivateChatRoomRequest
import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.dto.responce.PrivateChatRoomResponse
import com.example.ajaxproject.domain.PrivateChatMessage
import com.example.ajaxproject.domain.toResponse
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
    fun createRoom(@RequestBody privateRoomRequest: PrivateChatRoomRequest): Mono<PrivateChatRoomResponse> =
        privateChatService.createPrivateRoom(privateRoomRequest.senderId, privateRoomRequest.recipientId)
            .map { it.toResponse() }

    @GetMapping("/findRoom/{roomId}")
    fun findRoom(@PathVariable roomId: String): Mono<PrivateChatRoomResponse> =
        privateChatService.getPrivateRoom(roomId).map { it.toResponse() }

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody privateMessageDTO: PrivateMessageDTO): Mono<PrivateChatMessage> =
        privateChatService.sendPrivateMessage(privateMessageDTO)

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody privateChatRoomRequest: PrivateChatRoomRequest): Flux<PrivateChatMessage> =
        privateChatService.getAllPrivateMessages(privateChatRoomRequest)
}

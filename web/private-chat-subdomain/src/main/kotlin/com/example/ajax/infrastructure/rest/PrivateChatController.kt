package com.example.ajax.infrastructure.rest

import com.example.ajax.application.port.PrivateChatRoomServiceInPort
import com.example.ajax.domain.PrivateChatRoom
import com.example.ajax.infrastructure.dto.PrivateChatRoomRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/chat")
class PrivateChatController(
    val privateChatService: PrivateChatRoomServiceInPort
) {

    @PostMapping("/createRoom")
    fun createRoom(@RequestBody privateRoomRequest: PrivateChatRoomRequest): Mono<PrivateChatRoom> =
        privateChatService.createPrivateRoom(privateRoomRequest.senderId, privateRoomRequest.recipientId)

    @GetMapping("/findRoom/{roomId}")
    fun findRoom(@PathVariable roomId: String): Mono<PrivateChatRoom> =
        privateChatService.getPrivateRoom(roomId)
}

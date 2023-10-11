package com.example.ajaxproject.controller


import com.example.ajaxproject.dto.request.PrivateMessageDTO
import com.example.ajaxproject.dto.request.RoomDTO
import com.example.ajaxproject.model.PrivateChatMessage
import com.example.ajaxproject.model.PrivateChatRoom
import com.example.ajaxproject.service.interfaces.PrivateChatService
import org.springframework.http.ResponseEntity
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
    private val privateChatService: PrivateChatService
) {
    @PostMapping("/createRoom")
    fun createRoom(@RequestBody roomDTO: RoomDTO): Mono<ResponseEntity<PrivateChatRoom>> {
        return privateChatService.createPrivateRoom(roomDTO.senderId, roomDTO.recipientId)
            .map { ResponseEntity.ok(it) }
    }

    @GetMapping("/findRoom/{roomId}")
    fun findRoom(@PathVariable roomId: String): Mono<ResponseEntity<PrivateChatRoom?>> {
        return privateChatService.getPrivateRoom(roomId)
            .map { ResponseEntity.ok(it) }
    }

    @PostMapping("/sendMessage")
    fun sendMessage(@RequestBody privateMessageDTO: PrivateMessageDTO): Mono<ResponseEntity<PrivateChatMessage>> {
        return privateChatService.sendPrivateMessage(privateMessageDTO)
            .map { ResponseEntity.ok(it) }
    }

    @GetMapping("/getAllMessages")
    fun getAllMessages(@RequestBody roomDTO: RoomDTO): Mono<ResponseEntity<List<PrivateChatMessage>>> {
        return privateChatService.getAllPrivateMessages(roomDTO)
            .collectList()
            .map { ResponseEntity.ok(it) }
    }
}

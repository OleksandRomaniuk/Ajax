package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.EmailDTO
import com.example.ajaxproject.dto.responce.SendEmailResponse
import reactor.core.publisher.Mono

interface EmailSenderService {

    fun send(emailDTO: EmailDTO): Mono<SendEmailResponse>

    fun isValidEmail(email: String): Boolean
}
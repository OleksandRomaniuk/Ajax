package com.example.ajax.infrastructure.email

import com.example.ajax.infrastructure.email.dto.EmailRequest
import com.example.ajax.infrastructure.email.dto.EmailResponse
import reactor.core.publisher.Mono

interface EmailSenderServiceOutPort {

    fun send(emailRequest: EmailRequest): Mono<EmailResponse>

    fun isValidEmail(email: String): Boolean
}

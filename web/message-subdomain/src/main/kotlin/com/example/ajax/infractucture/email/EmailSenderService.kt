package com.example.ajax.infractucture.email

import com.example.ajax.infractucture.email.dto.EmailRequest
import com.example.ajax.infractucture.email.dto.EmailResponse
import reactor.core.publisher.Mono

interface EmailSenderService {

    fun send(emailRequest: EmailRequest): Mono<EmailResponse>

    fun isValidEmail(email: String): Boolean
}

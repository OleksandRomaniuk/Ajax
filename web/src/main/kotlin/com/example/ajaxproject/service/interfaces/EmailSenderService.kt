package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.EmailDTO
import com.example.ajaxproject.dto.responce.SendEmailResponse

fun interface EmailSenderService {

    fun send(emailDTO: EmailDTO): SendEmailResponse
}

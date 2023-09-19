package com.example.ajaxproject.service.interfaces

import com.example.ajaxproject.dto.request.EmailDTO
import com.example.ajaxproject.dto.responce.SendEmailResponce

fun interface EmailSenderService {
    fun send(emailDTO: EmailDTO): SendEmailResponce
}

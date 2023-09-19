package com.example.ajaxproject.email



fun interface EmailSenderService {
    fun send(email: Email): SendEmailResult
}

package com.example.ajaxproject.config

import com.example.ajaxproject.service.interfaces.EmailSenderService
import com.example.ajaxproject.service.EmailSenderServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class EmailSenderConfig {

    @Bean
    fun emailSender(javaMailSender: JavaMailSender): EmailSenderService {
        return EmailSenderServiceImpl(javaMailSender)
    }
}

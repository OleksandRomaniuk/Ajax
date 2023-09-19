package com.example.ajaxproject.email

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

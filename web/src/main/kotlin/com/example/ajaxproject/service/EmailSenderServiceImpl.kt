package com.example.ajaxproject.service

import com.example.ajaxproject.dto.request.EmailDTO
import com.example.ajaxproject.dto.responce.SendEmailResponse
import com.example.ajaxproject.service.interfaces.EmailSenderService
import jakarta.mail.internet.MimeMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
internal class EmailSenderServiceImpl(private val javaMailSender: JavaMailSender) : EmailSenderService {

    override fun send(emailDTO: EmailDTO): Mono<SendEmailResponse> {
        return Mono.defer {
            javaMailSender.send(generateMailMessage(emailDTO))
            logger.debug("Email sent successfully")
            Mono.just(SendEmailResponse(status = 200))
        }.onErrorResume { error ->
            logger.error("An unexpected error has occurred sending email", error)
            Mono.just(SendEmailResponse(status = 500, cause = error.message))
        }
    }

    private fun generateMailMessage(emailDTO: EmailDTO): MimeMessage {
        return MimeMessageHelper(javaMailSender.createMimeMessage()).apply {
            setFrom(emailDTO.from)
            setTo(emailDTO.to)
            emailDTO.subject?.let { setSubject(it) }
            setText(emailDTO.body, true)
        }.mimeMessage
    }
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EmailSenderServiceImpl::class.java)
    }
}

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
import reactor.core.scheduler.Schedulers

@Service
internal class EmailSenderServiceImpl(private val javaMailSender: JavaMailSender) : EmailSenderService {

    override fun send(emailDTO: EmailDTO): Mono<SendEmailResponse> {
        return Mono.fromCallable {
            javaMailSender.send(generateMailMessage(emailDTO))
            SendEmailResponse(status = 200)
        }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnSuccess {
                logger.debug("Email sent successfully")
            }
            .onErrorResume { error ->
                logger.error("An unexpected error has occurred sending email", error)
                Mono.just(SendEmailResponse(status = 500, cause = error.message))
            }
    }

    private fun generateMailMessage(emailDTO: EmailDTO): MimeMessage {
        return MimeMessageHelper(javaMailSender.createMimeMessage()).apply {
            setFrom(emailDTO.from)
            setTo(emailDTO.to)
            setSubject(emailDTO.subject)
            setText(emailDTO.body, true)
        }.mimeMessage
    }

    override fun isValidEmail(email: String): Boolean {
        return EMAIL_REGEX.matches(email)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EmailSenderServiceImpl::class.java)
        private val EMAIL_REGEX: Regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
    }
}

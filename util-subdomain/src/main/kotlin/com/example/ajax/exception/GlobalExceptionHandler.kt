package com.example.ajaxproject.exeption


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(ex: NoSuchElementException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleUserNotFoundException(ex: NotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }
    @ExceptionHandler(MailException::class)
    fun handleEmailException(ex: MailException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.message)
    }

    @ExceptionHandler(MessagingException::class)
    fun handleMessagingException(ex: MessagingException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.message)
    }

    @ExceptionHandler(WrongActionException::class)
    fun handleUserNotFoundException(ex: WrongActionException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.LOCKED).body(ex.message)
    }
}

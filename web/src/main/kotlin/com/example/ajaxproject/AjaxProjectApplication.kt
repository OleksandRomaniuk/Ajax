package com.example.ajaxproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.web.reactive.config.EnableWebFlux

@EnableWebFlux
@SpringBootApplication
class AjaxProjectApplication
fun main(args: Array<String>) {
    runApplication<AjaxProjectApplication>(*args)
}

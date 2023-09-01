package com.example.ajaxproject

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class AjaxProjectApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<AjaxProjectApplication>(*args)
}

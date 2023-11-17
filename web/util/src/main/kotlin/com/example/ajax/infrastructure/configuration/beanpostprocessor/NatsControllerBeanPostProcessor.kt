package com.example.ajax.infrastructure.configuration.beanpostprocessor

import com.example.ajax.infrastructure.adapters.nats.NatsController
import com.google.protobuf.GeneratedMessageV3
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component
import reactor.core.scheduler.Schedulers

@Component
class NatsControllerBeanPostProcessor : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
        if (bean is NatsController<*, *>) {
            initializeNatsController(bean)
        }
        return bean
    }

    fun <RequestT : GeneratedMessageV3, ResponseT : GeneratedMessageV3>
            initializeNatsController(controller: NatsController<RequestT, ResponseT>) {
        controller.connection.createDispatcher { message ->
            val parsedData = controller.parser.parseFrom(message.data)
            controller.handle(parsedData)
                .map { it.toByteArray() }
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe { controller.connection.publish(message.replyTo, it) }
        }.apply { subscribe(controller.subject) }
    }
}

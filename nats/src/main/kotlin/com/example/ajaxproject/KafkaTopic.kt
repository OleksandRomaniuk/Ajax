package com.example.ajaxproject


object KafkaTopic {

    private const val SERVICE_NAME = "chat"

    object User {
        private const val SUBDOMAIN = "user"

        const val DELETE = "$SERVICE_NAME.output.pubsub.$SUBDOMAIN.delete"

        const val UPDATE = "$SERVICE_NAME.output.pubsub.$SUBDOMAIN.update"
    }
}

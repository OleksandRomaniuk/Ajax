package com.example.ajaxproject.service.`interface`

interface ServiceInterface<T, ID> {

    fun findAll(): List<T>

    fun findById(id: ID): T

    fun create(entity: T): T

    fun update(id: ID, entity: T)

    fun delete(id: ID)

}
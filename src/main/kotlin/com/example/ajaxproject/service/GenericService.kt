package com.example.ajaxproject.service

interface GenericService<T, ID> {
    fun findById(id: ID): T?
    fun findAll(): List<T>
    fun create(entity: T): T
    fun update(id: ID, entity: T): T
    fun delete(id: ID)
    fun findByEmail(email: String): T?
}
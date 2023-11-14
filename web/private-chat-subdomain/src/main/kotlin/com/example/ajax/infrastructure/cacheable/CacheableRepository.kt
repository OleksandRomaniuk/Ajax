package com.example.ajax.infrastructure.cacheable

import reactor.core.publisher.Mono

interface CacheableRepository<T> {

    fun save(entity: T): Mono<T>

    fun findById(id: String): Mono<T>
}

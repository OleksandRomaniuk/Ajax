package com.example.ajaxproject.utils;

import reactor.core.publisher.Mono

fun <T : Any> Mono<T>.doMonoOnNext(onNext: (T) -> Mono<*>): Mono<T> = flatMap { onNext(it).thenReturn(it) }

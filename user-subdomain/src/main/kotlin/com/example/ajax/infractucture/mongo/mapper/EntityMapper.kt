package com.example.ajax.infractucture.mongo.mapper

interface EntityMapper<D, T , L>{
    fun mapToDomain(entity: T): D
    fun mapToEntity(domain: D): T
    fun mapToProto(entity: D): L
}


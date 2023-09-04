package com.example.ajaxproject.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable
import java.util.*

@NoRepositoryBean
interface GenericRepository<T, ID : Serializable> : JpaRepository<T, ID>

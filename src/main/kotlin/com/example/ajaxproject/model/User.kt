package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.bson.types.ObjectId

@Table(name = "users")
class User(

    @Id
    @Column(name = "id")
    var id: ObjectId,

    @Column(name = "email", length = 100)
    var email: String = "",

    @Column(name = "password", length = 100)
    var password: String = "",
)

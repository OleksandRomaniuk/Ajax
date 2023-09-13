package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.bson.types.ObjectId

@Table(name = "users")
class User(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    var id: ObjectId,

    @JsonProperty("email")
    @Column(name = "email", length = 100)
    var email: String = "",

    @JsonProperty("password")
    @Column(name = "password", length = 100)
    var password: String = "",
)

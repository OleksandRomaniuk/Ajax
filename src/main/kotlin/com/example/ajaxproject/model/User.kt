package com.example.ajaxproject.model

import com.example.ajaxproject.model.Enum.Role
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0,

    @JsonProperty("email")
    @Column(name = "email", length = 100)
    val email: String = "",

    @JsonProperty("password")
    @Column(name = "password", length = 100)
    val password: String = "",

    @JsonProperty("role")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role = Role.CUSTOME)


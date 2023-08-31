package com.example.ajaxproject.model

import com.example.ajaxproject.model.Enum.Role
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0,

    @JsonProperty("firstName")
    @Column(name = "first_name", length = 25)
    var firstName: String = "",

    @JsonProperty("secondName")
    @Column(name = "second_name", length = 25)
    var secondName: String = "",

    @JsonProperty("email")
    @Column(name = "email", length = 100)
    var email: String = "",

    @JsonProperty("password")
    @Column(name = "password", length = 100)
    var password: String = "",

    @JsonProperty("role")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role = Role.CUSTOME)


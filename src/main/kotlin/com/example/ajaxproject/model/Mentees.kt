package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "mentees")
class Mentees(

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,

    @JsonProperty("age")
    @Column(name = "age", length = 3)
    val age: Int = 0,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    var mentee: User = User()
)

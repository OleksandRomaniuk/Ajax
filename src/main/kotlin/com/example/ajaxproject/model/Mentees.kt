package com.example.ajaxproject.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "mentees")
class Mentees(
    @Id
    @JsonProperty("id")
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,

    @JsonProperty("age")
    @Column(name = "age", length = 3)
    val age: Int = 0,

    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    var mentee: User = User()
)